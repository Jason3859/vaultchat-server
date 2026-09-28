package dev.jason.project.spring.vc_server.ws;

import java.security.Principal;
import java.util.Map;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import dev.jason.project.spring.vc_server.core.service.DeviceService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import lombok.AllArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@AllArgsConstructor
public class VaultChatWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	private UserService userService;
	private DeviceService deviceService;

	@Override
	public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
			.addInterceptors(new WebSocketHandshakeInterceptor(userService, deviceService))
			.setHandshakeHandler(new DefaultHandshakeHandler() {
				@Override
				protected @NonNull Principal determineUser(
					@NonNull ServerHttpRequest request,
					@NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
					return (Principal) attributes.get("principal");
				}
			});
	}

	@Override
	public void configureMessageBroker(@NonNull MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}
}
