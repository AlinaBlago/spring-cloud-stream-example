package com.project.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@EnableBinding(Processor.class)
@Service
public class ProcessorService {
    private Logger logger = LoggerFactory.getLogger(ProcessorService.class);

    @StreamListener(Processor.INPUT)
    @SendTo(Processor.OUTPUT)
    public String process(String message) {
        logger.info("Message: " + message);
        return message.toUpperCase();

    }
}




