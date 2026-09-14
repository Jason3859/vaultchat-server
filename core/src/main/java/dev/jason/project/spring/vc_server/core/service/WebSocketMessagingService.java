package dev.jason.project.spring.vc_server.core.service;

public interface WebSocketMessagingService {

    void sendMessage(String uid, String destination, Object payload);
}
