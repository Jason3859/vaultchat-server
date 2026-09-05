package dev.jason.project.spring.vc_server.microservice.messaging.model;

import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User;

import java.time.LocalDateTime;

public record MessagePayload(String from, String to, String text, String timestamp) {

    public Message toMessage() {
        return new Message(
            null,
            new User(from, null, null, null),
            new User(to, null, null, null),
            text,
            LocalDateTime.parse(timestamp)
        );
    }
}
