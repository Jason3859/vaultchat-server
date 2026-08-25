package dev.jason.project.spring.vc_server.controller;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.microservice.device.controller.DeviceController;
import dev.jason.project.spring.vc_server.microservice.social.controller.SocialController;
import dev.jason.project.spring.vc_server.microservice.user.controller.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class VcRestController {

	@Autowired
	private UserController userController;

	@Autowired
	DeviceController deviceController;

	@Autowired
	SocialController socialController;
	
	@GetMapping
	public String home() {
		return "Hello, World!";
	}
	
	@PostMapping("register-user")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterUserDto registerUser(@RequestBody RegisterUserDto dto) {
		registerUser(dto.asUserDto());
		addDevice(dto.asDeviceDto());
		registerSocialUser(dto.uid());
		
		return dto;
	}
	
	private void registerUser(UserDto dto) {
		userController.register(dto);
	}
	
	private void addDevice(DeviceDto dto) {
		deviceController.addDevice(dto);
	}
	
	private void registerSocialUser(String uid) {
		socialController.register(uid);
	}
}
