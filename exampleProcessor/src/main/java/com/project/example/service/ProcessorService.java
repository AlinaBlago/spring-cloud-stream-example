package com.project.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;


@EnableBinding(Processor.class)
public class ProcessorService {
    private Logger logger = LoggerFactory.getLogger(ProcessorService.class);

    @Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
    public String transform(String message) {
        logger.info("Message: " + message);
        return message.toUpperCase();

    }
}




