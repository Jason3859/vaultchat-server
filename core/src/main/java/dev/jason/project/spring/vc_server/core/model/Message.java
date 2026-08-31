package dev.jason.project.spring.vc_server.core.model;

import java.time.LocalDateTime;

public record Message(String id, String from, String to, String text, LocalDateTime timestamp) {
}
