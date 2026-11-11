package dev.jason.project.spring.vc_server.ws;

import static dev.jason.project.spring.vc_server.core.model.User.Status.Offline;
import static dev.jason.project.spring.vc_server.core.model.User.Status.Online;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.model.WsDestination;
import dev.jason.project.spring.vc_server.core.service.SocialService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import dev.jason.project.spring.vc_server.core.service.WebSocketMessagingService;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private final UserService userService;
    private final SocialService socialService;
    private final WebSocketMessagingService webSocketMessagingService;

    @EventListener
    public void handleConnectEvent(SessionConnectEvent event) {
        String uid = getUidFromEvent(event);
        if (uid == null) throw new IllegalStateException();

        userService.setStatus(uid, Online);

        socialService.getConnections(uid)
        	.stream()
        	.forEach(connectionUid -> {
        		webSocketMessagingService.send(WsDestination.TOPIC_CONNECTION_UPDATE, connectionUid, createPayload(uid, Online));
        	});
    }

    @EventListener
    public void handleDisconnectedEvent(SessionDisconnectEvent event) {
        String uid = getUidFromEvent(event);
        if (uid == null) throw new IllegalStateException();

        userService.setStatus(uid, Offline);
        socialService.getConnections(uid)
        	.stream()
        	.forEach(connectionUid -> {
        		LocalDateTime lastSeen = userService.updateLastSeenToNow(uid);
        		webSocketMessagingService.send(WsDestination.TOPIC_CONNECTION_UPDATE, connectionUid, createPayload(uid, Offline));
        		webSocketMessagingService.send(WsDestination.TOPIC_CONNECTION_LAST_SEEN, connectionUid, createPayload(uid, lastSeen));
        	});
    }

    @Nullable
    private String getUidFromEvent(AbstractSubProtocolEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> attributes = accessor.getSessionAttributes();

        Principal principal = (Principal) attributes.get("principal");
		return principal.getName();
    }

    private Map<String, Object> createPayload(String uid, User.Status status) {
        return Map.of("uid", uid, "status", status);
    }
    
    private Map<String, Object> createPayload(String uid, LocalDateTime lastSeen) {
		return Map.of("uid", uid, "lastSeen", lastSeen);
	}
}
