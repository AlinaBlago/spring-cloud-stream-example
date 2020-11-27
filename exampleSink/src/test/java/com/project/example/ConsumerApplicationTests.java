package com.project.example;

import com.project.example.service.ConsumerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.AbstractMessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ConsumerService.class)
//@ImportAutoConfiguration(exclude = {
//		KafkaAutoConfiguration.class})
@EnableAutoConfiguration
@DirtiesContext
class ConsumerApplicationTests {

	@Autowired
	private AbstractMessageChannel input;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SpyBean(name = "jdbcHandler")
	private MessageHandler jdbcMessageHandler;

	@Test
	void testMessages() {
		AtomicReference<Message<?>> messageAtomicReference = new AtomicReference<>();

		ChannelInterceptor assertionInterceptor =
				new ChannelInterceptor() {

					@Override
					public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent,
													Exception ex) {

						messageAtomicReference.set(message);
					}

				};

		this.input.addInterceptor(assertionInterceptor);

		this.input.send(new GenericMessage<>("odd"));

		this.input.removeInterceptor(assertionInterceptor);

		this.input.send(new GenericMessage<>("even"));

		List<Map<String, Object>> data = this.jdbcTemplate.queryForList("SELECT * FROM Message");

		assertEquals(data.size(), 2);
		assertEquals(data.get(0).get("message"), "odd");
		assertEquals(data.get(1).get("message"), "even");

		Message<?> message1 = messageAtomicReference.get();
		assertNotNull(message1);

	}

}