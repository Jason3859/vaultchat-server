package dev.jason.project.spring.vc_server.ws;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.service.UserService;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.Map;

import static dev.jason.project.spring.vc_server.core.model.User.Status.Offline;
import static dev.jason.project.spring.vc_server.core.model.User.Status.Online;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private static final String TOPIC_CONNECTIONS_STATUS = "/topic/connections-status";

    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        String uid = getUidFromEvent(event);
        if (uid == null) throw new IllegalStateException();

        userService.setStatus(uid, Online);
        simpMessagingTemplate.convertAndSendToUser(uid, TOPIC_CONNECTIONS_STATUS, createPayload(uid, Online));
    }

    @EventListener
    public void handleDisconnectedEvent(SessionDisconnectEvent event) {
        String uid = getUidFromEvent(event);
        if (uid == null) throw new IllegalStateException();

        userService.setStatus(uid, Offline);
        simpMessagingTemplate.convertAndSendToUser(uid, TOPIC_CONNECTIONS_STATUS, createPayload(uid, Offline));
    }

    @Nullable
    private String getUidFromEvent(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attributes = accessor.getSessionAttributes();

        if (attributes != null) {
            Principal principal = (Principal) attributes.get("principal");
            return principal.getName();
        }
        return null;
    }

    private Map<String, Object> createPayload(String uid, User.Status status) {
        return Map.of("uid", uid, "status", status);
    }
}
