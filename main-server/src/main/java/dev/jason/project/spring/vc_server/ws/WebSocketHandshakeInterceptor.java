package dev.jason.project.spring.vc_server.ws;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import dev.jason.project.spring.vc_server.core.service.DeviceService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private UserService userService;
    private DeviceService deviceService;

    @Override
    public boolean beforeHandshake(
        @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {

        Map<String, String> queryParams = getQueryParams(request);

        String uid = queryParams.get("uid");
        String deviceId = queryParams.get("device_id");

        if (uid == null || userService.doesUserNotExist(uid)) return false;
        if (deviceId == null || !deviceService.doesDeviceExist(deviceId)) return false;

        attributes.put("principal", (Principal) () -> uid);

        return true;
    }

    @Override
    public void afterHandshake(
        @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler, @Nullable Exception exception) {

        Map<String, String> queryParams = getQueryParams(request);
        String deviceId = queryParams.get("device_id");

        deviceService.updateLastUsed(deviceId, LocalDateTime.now());
    }

    private static @NonNull Map<String, String> getQueryParams(@NonNull ServerHttpRequest request) {
        return UriComponentsBuilder.fromUri(request.getURI())
            .build()
            .getQueryParams()
            .asSingleValueMap();
    }
}
