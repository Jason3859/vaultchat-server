package dev.jason.project.spring.vc_server.microservice.messaging.controller;

import dev.jason.project.spring.vc_server.core.dto.MessageDto;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.service.MessagesDatabaseService;
import dev.jason.project.spring.vc_server.core.service.SocialService;
import dev.jason.project.spring.vc_server.microservice.messaging.model.MessagePayload;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class MessagingController {

    private static final String TOPIC_MESSAGES = "/topic/messages";

    private final SocialService socialService;
    private final MessagesDatabaseService messagesDatabaseService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/messaging.send")
    public void sendMessage(@Payload MessagePayload messagePayload) {
        boolean isUserBlocked = socialService.isUserBlocked(messagePayload.from(), messagePayload.to());
        if (isUserBlocked) return;

        socialService.tryConnect(messagePayload.from(), messagePayload.to());

        Message message = messagesDatabaseService.addMessage(messagePayload.toMessage());
        MessageDto responseBody = MessageDto.fromMessage(message);

        messagingTemplate.convertAndSendToUser(message.from().uid(), TOPIC_MESSAGES, responseBody);
        messagingTemplate.convertAndSendToUser(message.to().uid(), TOPIC_MESSAGES, responseBody);
    }

    @ResponseBody
    @GetMapping("/messages/fetch")
    public ResponseEntity<?> fetchMessages(@RequestParam String uid) {
        List<Message> messages = messagesDatabaseService.getMessages(uid);
        HttpStatus status;

        if (messages.isEmpty()) {
            status = HttpStatus.NO_CONTENT;
        } else {
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(messages, status);
    }
}
