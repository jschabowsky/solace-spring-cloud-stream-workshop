package com.solace.workshop.demo.app;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProcessorBinding {
    String INPUT = "input";

    @Input
    SubscribableChannel input();

    @Output
    MessageChannel outputSensor1();

    @Output
    MessageChannel outputSensor2();    
}

