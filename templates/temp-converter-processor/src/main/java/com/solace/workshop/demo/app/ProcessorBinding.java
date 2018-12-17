package com.solace.workshop.demo.app;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProcessorBinding {
    String INPUT = "input";
    String OUTPUT_SENSOR1 = "output_sensor1";
    String OUTPUT_SENSOR2 = "output_sensor2";

    // Add one input and two outputs corresponding to the above
    
}

