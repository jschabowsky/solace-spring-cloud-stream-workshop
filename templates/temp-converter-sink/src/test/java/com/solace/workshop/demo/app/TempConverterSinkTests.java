package com.solace.workshop.demo.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempConverterSinkTests {
    private static final Logger _log = LoggerFactory.getLogger(TempConverterSinkTests.class);

    @Autowired
    private Sink sink;

    @Test
    public void testSink() {        
        sink.input().send(MessageBuilder.withPayload("test").build());

        _log.info("Sent test message to sink");
    }

}

