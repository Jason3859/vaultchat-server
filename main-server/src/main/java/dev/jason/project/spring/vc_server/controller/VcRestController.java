package dev.jason.project.spring.vc_server.controller;

import dev.jason.project.spring.vc_server.core.exception.VcException;
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
	
	@PostMapping("register")
	@ResponseStatus(HttpStatus.CREATED)
	public RegisterUserDto registerUser(@RequestBody RegisterUserDto dto) {

		try {
			userController.register(dto.asUserDto());
			socialController.register(dto.uid());
		} catch (VcException.UserException.UserAlreadyExistsException _) {
			// ignore this and continue adding the new device to db
		}

		deviceController.addDevice(dto.asDeviceDto());
		return dto;
	}
}
