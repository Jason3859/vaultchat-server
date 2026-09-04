package dev.jason.project.spring.vc_server.core.dto;

import java.time.LocalDateTime;

public record MessageDto(String from, String fromDisplayName, String to, String text, LocalDateTime timestamp) {
}
