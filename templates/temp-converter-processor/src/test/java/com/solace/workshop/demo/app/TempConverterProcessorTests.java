package com.solace.workshop.demo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import com.solace.workshop.demo.datamodel.SensorReading;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempConverterProcessorTests {
    private static final Logger _log = LoggerFactory.getLogger(TempConverterProcessorTests.class);

    @Autowired
    private ProcessorBinding processor;

    @Autowired
    private MessageCollector collector;
    
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();
        
	@Test
	public void testMissingHeader() {
		exceptionRule.expect(MessagingException.class);
		exceptionRule.expectMessage("Missing or invalid BASE_UNIT header");

        SensorReading reading = new SensorReading("SENSOR1", 0, SensorReading.BaseUnit.CELSIUS);
        
        Message<SensorReading> msgInput = MessageBuilder
			.withPayload(reading)
			.build();
		processor.input().send(msgInput);
	}
 
	@Test
	public void testInvalidTemperature() {
		exceptionRule.expect(MessagingException.class);
		exceptionRule.expectMessage("Temperature cannot be below absolute zero");

        SensorReading reading = new SensorReading("SENSOR1", -500, SensorReading.BaseUnit.CELSIUS);
        
        Message<SensorReading> msgInput = MessageBuilder
			.withPayload(reading)
			.setHeader("BASE_UNIT", SensorReading.BaseUnit.CELSIUS.toString())			
			.build();		
		processor.input().send(msgInput);
	}
	
    @Test
    public void testProcessorSensor1() throws InterruptedException {
        SensorReading reading = new SensorReading("SENSOR1", 55.3, SensorReading.BaseUnit.CELSIUS);
        
        Message<SensorReading> msgInput = MessageBuilder
			.withPayload(reading)
			.setHeader("BASE_UNIT", SensorReading.BaseUnit.CELSIUS.toString())
			.build();
		
        processor.input().send(msgInput);
        
        Message<?> msgOutput = (Message<?>) collector.forChannel(processor.outputSensor1()).poll(10, TimeUnit.SECONDS);
        String payload = (msgOutput != null) ? (String)msgOutput.getPayload() : null;        
        _log.info("Sensor 1 payload: " + payload);
        
        assertNotNull(payload);
        assertThat(payload, allOf(
        		containsString("SENSOR1"),
        		containsString("55.3"),
        		containsString("CELSIUS")
        		));        
        
    }
    
	@Test
	public void testProcessorSensor2() {
		SensorReading reading = new SensorReading("SENSOR2", 11.5, SensorReading.BaseUnit.FAHRENHEIT);
		Message<SensorReading> msgInput = MessageBuilder
			.withPayload(reading)
			.setHeader("BASE_UNIT", SensorReading.BaseUnit.FAHRENHEIT.toString())
			.build();
        
        processor.input().send(msgInput);
        
        Message<?> msgOutput = (Message<?>) collector.forChannel(processor.outputSensor2()).poll();
        String payload = (msgOutput != null) ? (String)msgOutput.getPayload() : null;
        _log.info("Sensor 2 payload: {}", payload);

        assertNotNull(payload);        
        assertThat(payload, allOf(
        		containsString("SENSOR2"),
        		containsString("-11.38"),
        		containsString("CELSIUS")
        		));
	}    
}

