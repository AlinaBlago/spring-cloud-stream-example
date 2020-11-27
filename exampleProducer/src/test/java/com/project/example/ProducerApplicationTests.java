package com.project.example;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.cloud.stream.test.matcher.MessageQueueMatcher.receivesPayloadThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.cloud.stream.poller.fixed-delay=1")
@DirtiesContext
public class ProducerApplicationTests {

    @Autowired
    private Source channels;

    @Autowired
    private MessageCollector collector;

    @Test
    public void testMessages() {
        Message<String> message = new GenericMessage<>("hello");
        this.channels.output().send(message);
        BlockingQueue<Message<?>> messages = collector.forChannel(channels.output());

        assertEquals(messages, receivesPayloadThat(is("hello")));


    }



}




