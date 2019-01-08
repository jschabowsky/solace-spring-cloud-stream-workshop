package com.solace.workshop.demo.app;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempConverterSourceTests {
    @Autowired
    private Source source;

    @Autowired
    private MessageCollector collector;
    
	@Test
    public void testSource() throws InterruptedException {
        Message<?> msg = (Message<?>) collector.forChannel(source.output()).poll(10, TimeUnit.SECONDS);
        Object payload = (msg != null) ? msg.getPayload() : null;        

        assertNotNull(payload);
        assertThat((String)payload, allOf(
        		containsString("timestamp"),
        		containsString("sensorID"),
        		containsString("temperature"),
        		containsString("baseUnit")));
    }

}

