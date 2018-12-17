package com.solace.workshop.demo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.solace.workshop.demo.datamodel.SensorReading;

/**
 * Sample Solace Spring Cloud Stream source
 *
 * @author Solace Corp
 */
@SpringBootApplication
@EnableBinding(Source.class)
public class TempConverterSource {
    private static final Logger _log = LoggerFactory.getLogger(TempConverterSource.class);
    private static final Random _random = new Random(System.currentTimeMillis());
    private static final int RANDOM_MULTIPLIER = 100;
        
    private AtomicBoolean semaphore = new AtomicBoolean(true);
	
	public static void main(String[] args) {
		SpringApplication.run(TempConverterSource.class, args);
	}
    
    @InboundChannelAdapter(channel = Source.OUTPUT, poller = @Poller(fixedRate = "1000")) 
    public Message<?> emitSensorReading() {
        double temperature = _random.nextDouble() * RANDOM_MULTIPLIER;
        String sensorID = null;
        SensorReading.BaseUnit baseUnit = SensorReading.BaseUnit.CELSIUS;

        if (this.semaphore.getAndSet(!this.semaphore.get())) {
            sensorID = "SENSOR1";
        } else {
            sensorID = "SENSOR2";
            baseUnit = SensorReading.BaseUnit.FAHRENHEIT;
        }

        SensorReading reading = new SensorReading(sensorID, temperature, baseUnit);
        _log.info(reading.toString());

        return MessageBuilder
        		.withPayload(reading)
        		.setHeader("BASE_UNIT", baseUnit.toString())
        		.build();
    }


}
