package dev.jason.project.spring.vc_server.controller;

import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.microservice.device.controller.DeviceController;
import dev.jason.project.spring.vc_server.microservice.social.controller.SocialController;
import dev.jason.project.spring.vc_server.microservice.user.controller.UserController;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class VcRestController {

	private UserController userController;
	private DeviceController deviceController;
	private SocialController socialController;
	
	@GetMapping
	public String home() {
		return "Hello, World!";
	}
	
	@PostMapping("register-user")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterUserDto registerUser(@RequestBody RegisterUserDto dto) {
        userController.register(dto.asUserDto());
        deviceController.addDevice(dto.asDeviceDto());
		socialController.register(dto.uid());
		return dto;
	}
}
