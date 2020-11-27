package com.project.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleApplicationTests.ProcessorService.class, webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
		"--spring.cloud.stream.bindings.input.contentType=text/plain",
		"--spring.cloud.stream.bindings.output.contentType=text/plain" })
@DirtiesContext
public class ExampleApplicationTests {
	@Autowired
	private MessageCollector collector;

	@Autowired
	private Processor processor;

	@Test
	@SuppressWarnings("unchecked")
	public void testWiring() {
		Message<String> message = new GenericMessage<>("hello");
		this.processor.input().send(message);
		Message<String> received = (Message<String>) this.collector
				.forChannel(this.processor.output()).poll();
		assertEquals("HELLO", received.getPayload());
	}

	@SpringBootApplication
	@EnableBinding(Processor.class)
	public static class ProcessorService {

		@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
		public String transform(String in) {
			return in.toUpperCase();
		}

	}

}




