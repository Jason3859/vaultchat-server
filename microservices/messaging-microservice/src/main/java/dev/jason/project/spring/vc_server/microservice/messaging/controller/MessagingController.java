package dev.jason.project.spring.vc_server.microservice.messaging.controller;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.model.User.Status;
import dev.jason.project.spring.vc_server.core.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoints.MESSAGING)
public class MessagingController {

	@Autowired
	private MessagingService messagingService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }
	
	@PostMapping(Endpoints.SEND)
	@ResponseStatus(HttpStatus.OK)
	public void send(@RequestBody Message message) {
//		messagingService.sendMessage(message);
	}
	
	@PostMapping(Endpoints.NOTIFY_STATUS)
	public void notifyStatus(@RequestParam String uid, @RequestParam Status status) {
		messagingService.sendStatusUpdate(uid, status);
	}
	
	@PostMapping(Endpoints.LOGOUT)
	public void logout(@RequestParam boolean clearMessages, @RequestBody DeviceDto deviceDto) {
//		messagingService.logout(deviceDto.toDevice(null), clearMessages);
	}
	
}
