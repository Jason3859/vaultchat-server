package dev.jason.project.spring.vc_server.microservice.messaging.controller

import dev.jason.project.spring.vc_server.core.dto.MessageDto
import dev.jason.project.spring.vc_server.core.service.CheckIfUserIsBlockedService
import dev.jason.project.spring.vc_server.core.service.GetUserService
import dev.jason.project.spring.vc_server.core.service.MessageService
import dev.jason.project.spring.vc_server.microservice.messaging.model.MessagePayload
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class MessagesController {

    companion object {
        private const val TOPIC_MESSAGES = "/topic/messages"
    }

    @Autowired private lateinit var messageService: MessageService
    @Autowired private lateinit var getUserService: GetUserService
    @Autowired private lateinit var messagingTemplate: SimpMessagingTemplate
    @Autowired private lateinit var checkIfUserIsBlockedService: CheckIfUserIsBlockedService

    @Suppress("unused")
    @MessageMapping("/message.send")
    fun sendMessage(@Payload messagePayload: MessagePayload) {
        val userBlocked = checkIfUserIsBlockedService.isUserBlocked(messagePayload.from, messagePayload.to)
        if (userBlocked) return

        val message = messageService.addMessage(messagePayload.toMessage())
        val responseBody = MessageDto.fromMessage(message)

        messagingTemplate.convertAndSendToUser(message.from.uid, TOPIC_MESSAGES, responseBody)
        messagingTemplate.convertAndSendToUser(message.to.uid, TOPIC_MESSAGES, responseBody)
    }

    @ResponseBody
    @GetMapping("/messages/fetch")
    fun fetchMessages(@RequestParam uid: String): ResponseEntity<*> {
        val messages = messageService.getMessages(uid)
        val status = if (messages.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK

        return ResponseEntity(messages, status)
    }
}
