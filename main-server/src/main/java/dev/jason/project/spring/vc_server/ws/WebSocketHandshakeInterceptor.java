package dev.jason.project.spring.vc_server.ws;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.service.UserService;
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

    private UserService userService;

    @Override
    public boolean beforeHandshake(
        @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {

        String uid = extractUidFromRequest(request);

        if (uid == null || !userService.doesUserExist(uid)) return false;

        attributes.put("principal", (Principal) () -> uid);
        return true;
    }

    @Override
    public void afterHandshake(
        @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler, @Nullable Exception exception) {

        String uid = extractUidFromRequest(request);
        userService.setStatus(uid, User.Status.Online);
    }

    private String extractUidFromRequest(ServerHttpRequest request) {
        String query = request.getURI().getQuery();

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
