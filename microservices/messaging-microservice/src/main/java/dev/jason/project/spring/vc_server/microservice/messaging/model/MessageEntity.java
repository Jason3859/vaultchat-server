package dev.jason.project.spring.vc_server.microservice.messaging.model;

import dev.jason.project.spring.vc_server.core.model.Message;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("messages")
public record MessageEntity(
	@Id String id,
	String from,
	String to,
	String text,
	LocalDateTime timestamp
) {

    public static MessageEntity fromMessage(Message message) {
		return new MessageEntity(message.id(), message.from(), message.to(), message.text(), message.timestamp());
	}
	
	public Message toMessage() {
		return new Message(id, from, to, text, timestamp);
	}
}
