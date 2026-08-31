package dev.jason.project.spring.vc_server.core.model;

public record Message(String id, String from, String to, String text, String timestamp) {
}
