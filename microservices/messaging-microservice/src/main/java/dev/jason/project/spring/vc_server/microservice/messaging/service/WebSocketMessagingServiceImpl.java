package dev.jason.project.spring.vc_server.microservice.messaging.service;

import dev.jason.project.spring.vc_server.core.model.WsDestination;
import dev.jason.project.spring.vc_server.core.service.WebSocketMessagingService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WebSocketMessagingServiceImpl implements WebSocketMessagingService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void send(WsDestination destination, String uid, Object payload) {
        simpMessagingTemplate.convertAndSendToUser(uid, destination.toString(), payload);
    }
}
