package dev.jason.project.spring.vc_server.microservice.messaging.controller;

import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class MessagesController {

	private MessageService messageService;
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@MessageMapping("/send")
	@SuppressWarnings("unused") // for IntelliJ IDEA
	public Message sendMessage(Message message) {
		messageService.addMessage(message);
		simpMessagingTemplate.convertAndSend("/topic/messages/" + message.from(), message);
		simpMessagingTemplate.convertAndSend("/topic/messages/" + message.to(), message);
		return message;
	}

	@ResponseBody
	@GetMapping("/messages/fetch")
	public ResponseEntity<?> fetchMessages(@RequestParam String uid) {
		var messages = messageService.getMessages(uid);
		var status = messages.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

		return new ResponseEntity<>(messages, status);
	}
}
