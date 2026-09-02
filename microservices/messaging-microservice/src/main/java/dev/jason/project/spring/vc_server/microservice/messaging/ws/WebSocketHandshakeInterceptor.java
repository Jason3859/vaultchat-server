package dev.jason.project.spring.vc_server.microservice.messaging.ws;

import dev.jason.project.spring.vc_server.core.service.GetUserService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

@AllArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private GetUserService getUserService;

    @Override
    public boolean beforeHandshake(
        @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {

        String query = request.getURI().getQuery();
        String uid = extractUid(query);

        if (uid == null || !getUserService.doesUserExist(uid)) {
            return false;
        }

        attributes.put("principal", (Principal) () -> uid);
        return true;
    }

    @Override
    public void afterHandshake(
        @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler, @Nullable Exception exception) {
    }

    private String extractUid(String query) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && pair[0].equals("uid")) {
                return pair[1];
            }
        }
        return null;
    }
}
