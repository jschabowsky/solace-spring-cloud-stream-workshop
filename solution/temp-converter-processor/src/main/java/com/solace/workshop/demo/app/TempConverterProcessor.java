package com.solace.workshop.demo.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.solace.workshop.demo.datamodel.SensorReading;

/**
 * Sample Solace Spring Cloud Stream processor
 *
 * @author Solace Corp
 */
@SpringBootApplication
@EnableBinding(ProcessorBinding.class)
public class TempConverterProcessor {
    private static final Logger _log = LoggerFactory.getLogger(TempConverterProcessor.class);
    private static final double MIN_TEMP_CELSIUS = -273.15; 
    
    @Autowired
    private ProcessorBinding processor;
    
    private static final <T> Message<T> message(T val) {
        return MessageBuilder.withPayload(val).build();
    }
    
    private void validateTemperature(double temperatureCelsius) {
        if (temperatureCelsius < MIN_TEMP_CELSIUS) {
            throw new IllegalArgumentException("Temperature cannot be below absolute zero");
        }
    }
    
    @StreamListener(ProcessorBinding.INPUT)
    public void validateReading(Message<?> msg) {     
        String baseUnit = (String)msg.getHeaders().get("BASE_UNIT");
        
        if (baseUnit == null || 
                (!baseUnit.equals(SensorReading.BaseUnit.CELSIUS.toString()) &&
                !baseUnit.equals(SensorReading.BaseUnit.FAHRENHEIT.toString()))) { 
            throw new IllegalArgumentException("Missing or invalid BASE_UNIT header");
        }
    }

    @StreamListener(target = ProcessorBinding.INPUT, condition = "headers['BASE_UNIT']=='CELSIUS'")
    public void passThruMetric(SensorReading reading) {
        _log.info("passThruMetric: " + reading);

        validateTemperature(reading.getTemperature());

        processor.outputSensor1().send(message(reading));
    }
    
    @StreamListener(target = ProcessorBinding.INPUT, condition = "headers['BASE_UNIT']=='FAHRENHEIT'")
    public void convertFtoC(SensorReading reading) {
        _log.info("convertFtoC (pre-conv): " + reading);
                
        double temperatureCelsius = (reading.getTemperature().doubleValue() - 32) * 5 / 9;

        validateTemperature(temperatureCelsius);
        
        reading.setTemperature(temperatureCelsius);
        reading.setBaseUnit(SensorReading.BaseUnit.CELSIUS);
        
        _log.info("convertFtoC (post-conv): " + reading);
        
        processor.outputSensor2().send(message(reading));
    }    

    public static void main(String[] args) {
        SpringApplication.run(TempConverterProcessor.class, args);
    }
}
