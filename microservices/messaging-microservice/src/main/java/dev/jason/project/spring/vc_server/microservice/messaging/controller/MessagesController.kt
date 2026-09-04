package dev.jason.project.spring.vc_server.microservice.messaging.controller

import dev.jason.project.spring.vc_server.core.model.Message
import dev.jason.project.spring.vc_server.core.service.MessageService
import dev.jason.project.spring.vc_server.microservice.messaging.events.MessagingEvent
import dev.jason.project.spring.vc_server.microservice.messaging.events.MessagingEvents
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
    @Autowired private lateinit var messagingTemplate: SimpMessagingTemplate

    @MessageMapping("/send")
    @Suppress("unused") // for IntelliJ IDEA
    fun sendMessage(@Payload message: Message): Message {
        val msg = messageService.addMessage(message)
        messagingTemplate.convertAndSendToUser(message.from, "/topic/messages", msg)
        messagingTemplate.convertAndSendToUser(message.to, "/topic/messages", msg)
        return message
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
                    messagingTemplate.convertAndSendToUser(
                        event.toUid,
                        "/topic/connections-status",
                        payload
                    )
                }
            }
        }
    }
}
