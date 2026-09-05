package dev.jason.project.spring.vc_server.core.model;

import java.time.LocalDateTime;

public record Message(
    String id,
    User from,
    User to,
    String text,
    LocalDateTime timestamp
) {
}
