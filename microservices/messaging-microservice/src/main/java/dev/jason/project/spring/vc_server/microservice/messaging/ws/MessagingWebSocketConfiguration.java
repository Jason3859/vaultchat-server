package dev.jason.project.spring.vc_server.microservice.messaging.ws;

import dev.jason.project.spring.vc_server.core.service.GetUserService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@AllArgsConstructor
public class MessagingWebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	private GetUserService getUserService;

	@Override
	public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
		registry.addEndpoint("/messages")
			.addInterceptors(new WebSocketHandshakeInterceptor(getUserService))
			.setHandshakeHandler(new DefaultHandshakeHandler() {
				@Override
				protected @Nullable Principal determineUser(
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
