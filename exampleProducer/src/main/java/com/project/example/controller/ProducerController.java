package com.project.example.controller;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@EnableBinding(Source.class)
@RestController
public class ProducerController {
    private final MessageChannel output;
    final Pattern pattern = Pattern.compile("^[A-Za-z, ]++$");

    public ProducerController(MessageChannel output) {
        this.output = output;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/send")
    public void publishMessage(@RequestParam("message") String message) {
        if (!pattern.matcher(message).matches()) {
            throw new IllegalArgumentException("Invalid String");
        } else {
            output.send(MessageBuilder.withPayload(message).build());
        }
    }

}
