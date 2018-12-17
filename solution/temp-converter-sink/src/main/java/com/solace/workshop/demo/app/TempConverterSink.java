package com.solace.workshop.demo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * Sample Solace Spring Cloud Stream sink
 *
 * @author Solace Corp
 */
@SpringBootApplication
@EnableBinding(Sink.class)
public class TempConverterSink {
    private static final Logger _log = LoggerFactory.getLogger(TempConverterSink.class);

    @StreamListener(Sink.INPUT)
    public void sink(String input) {
		_log.info(input);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TempConverterSink.class, args);
	}
}
