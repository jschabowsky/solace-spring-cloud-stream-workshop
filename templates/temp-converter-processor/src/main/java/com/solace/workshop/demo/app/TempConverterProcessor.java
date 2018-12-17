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
    	// If temperature is below absolute zero, throw an exception
    }
    
    @StreamListener(ProcessorBinding.INPUT)
    public void validateReading(Message<?> msg) {     
    	// If the base unit header is missing or invalid, throw an exception
    }

    @StreamListener(target = ProcessorBinding.INPUT, condition = "headers['BASE_UNIT']=='CELSIUS'")
    public void passThruMetric(SensorReading reading) {
		_log.info("passThruMetric: " + reading);

		// Validate the reading, then publish to the sensor1 output
    }
	
    @StreamListener(target = ProcessorBinding.INPUT, condition = "headers['BASE_UNIT']=='FAHRENHEIT'")
    public void convertFtoC(SensorReading reading) {
    	// Calculate the conversion from F to C degrees
    	// Validate the result, then publish to the sensor2 output
	}    

	public static void main(String[] args) {
		SpringApplication.run(TempConverterProcessor.class, args);
	}
}
