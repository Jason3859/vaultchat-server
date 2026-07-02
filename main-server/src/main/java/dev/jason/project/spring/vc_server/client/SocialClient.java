package dev.jason.project.spring.vc_server.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dev.jason.project.spring.vc_server.core.Endpoints;

@FeignClient(name = "social-microservice", url = "${app.uri.social-microservice}")
public interface SocialClient {

	@PostMapping(Endpoints.SOCIAL_REGISTER)
	void registerUser(@RequestParam String uid);
}
