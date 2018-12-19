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
// TODO: Add custom processor binding
public class TempConverterProcessor {
	private static final Logger _log = LoggerFactory.getLogger(TempConverterProcessor.class);
	private static final double MIN_TEMP_CELSIUS = -273.15; 
	
	// TODO: Add variable for the custom processor
	
    private static final <T> Message<T> message(T val) {
        return MessageBuilder.withPayload(val).build();
    }
    
    private void validateTemperature(double temperatureCelsius) {
    	// TODO: If temperature is below absolute zero, throw an exception
    }
    
    @StreamListener(ProcessorBinding.INPUT)
    public void validateReading(Message<?> msg) {     
    	// TODO: If the base unit header is missing or invalid, throw an exception
    }

    // TODO: Add a stream listener which routes content in metric units 
    public void passThruMetric(SensorReading reading) {
		_log.info("passThruMetric: " + reading);

		// TODO: Validate the reading, then publish to the sensor1 output
    }
	
    // TODO: Add a stream listener which routes content in imperial units
    public void convertFtoC(SensorReading reading) {
    	// TODO: Calculate the conversion from F to C degrees
    	// TODO: Validate the result, then publish to the sensor2 output
	}    

	public static void main(String[] args) {
		SpringApplication.run(TempConverterProcessor.class, args);
	}
}
