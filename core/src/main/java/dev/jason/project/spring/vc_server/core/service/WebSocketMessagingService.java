package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.WsDestination;

public interface WebSocketMessagingService {

    void send(WsDestination destination, String uid, Object payload);
}
