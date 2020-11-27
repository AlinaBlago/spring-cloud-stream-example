package com.project.example.service;

import com.project.example.entity.Message;
import com.project.example.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@EnableBinding(Sink.class)
public class ConsumerService {
    private final MessageRepository messageRepository;
    private Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    public ConsumerService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @StreamListener(target = Sink.INPUT)
    public void consume(String message){
        logger.info("Message: " + message);
        Message message1 = new Message();
        message1.setMessage(message);
        messageRepository.saveAndFlush(message1);
    }
}
