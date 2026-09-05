package dev.jason.project.spring.vc_server.microservice.messaging.controller

import dev.jason.project.spring.vc_server.core.dto.MessageDto
import dev.jason.project.spring.vc_server.core.service.GetUserService
import dev.jason.project.spring.vc_server.core.service.MessageService
import dev.jason.project.spring.vc_server.microservice.messaging.events.MessagingEvent
import dev.jason.project.spring.vc_server.microservice.messaging.events.MessagingEvents
import dev.jason.project.spring.vc_server.microservice.messaging.model.MessagePayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @Autowired private lateinit var messageService: MessageService
    @Autowired private lateinit var getUserService: GetUserService
    @Autowired private lateinit var messagingTemplate: SimpMessagingTemplate

    @Suppress("unused")
    @MessageMapping("/send")
    fun sendMessage(@Payload messagePayload: MessagePayload) {
        val message = messageService.addMessage(messagePayload.toMessage())
        val payload = MessageDto.fromMessage(message)

        messagingTemplate.convertAndSendToUser(message.from.uid, "/topic/messages", payload)
        messagingTemplate.convertAndSendToUser(message.to.uid, "/topic/messages", payload)
    }

    @ResponseBody
    @GetMapping("/messages/fetch")
    fun fetchMessages(@RequestParam uid: String): ResponseEntity<*> {
        val messages = messageService.getMessages(uid)
        val status = if (messages.isEmpty()) HttpStatus.NO_CONTENT else HttpStatus.OK

        return ResponseEntity(messages, status)
    }

    init {
        coroutineScope.launch {
            MessagingEvents.events.collect { event ->
                if (event is MessagingEvent.UserStatusUpdate) {
                    val payload = mapOf("uid" to event.userUid, "status" to event.status)
                    messagingTemplate.convertAndSendToUser(event.toUid, "/topic/connections-status", payload)
                }
            }
        }
    }
}
