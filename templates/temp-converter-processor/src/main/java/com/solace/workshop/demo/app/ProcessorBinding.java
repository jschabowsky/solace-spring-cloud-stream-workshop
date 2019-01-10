package com.solace.workshop.demo.app;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProcessorBinding {
    String INPUT = "input";

    // TODO: Add one input and two outputs corresponding to the above
    
}

