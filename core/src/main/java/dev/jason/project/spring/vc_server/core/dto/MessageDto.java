package dev.jason.project.spring.vc_server.core.dto;

import dev.jason.project.spring.vc_server.core.model.Message;

import java.time.LocalDateTime;

public record MessageDto(
    String id,
    UserDto from,
    UserDto to,
    String text,
    LocalDateTime timestamp
) {

    public static MessageDto fromMessage(Message message) {
        return new MessageDto(
            message.id(),
            UserDto.fromUser(message.from()),
            UserDto.fromUser(message.to()),
            message.text(),
            message.timestamp()
        );
    }
}
