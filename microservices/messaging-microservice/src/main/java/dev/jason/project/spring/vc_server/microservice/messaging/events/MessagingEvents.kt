package dev.jason.project.spring.vc_server.microservice.messaging.events

import dev.jason.project.spring.vc_server.core.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object MessagingEvents {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val _events = MutableSharedFlow<MessagingEvent>()
    val events = _events.asSharedFlow()

    @JvmStatic
    fun sendEvent(event: MessagingEvent) {
        coroutineScope.launch {
            _events.emit(event)
        }
    }
}

sealed interface MessagingEvent {
    data class UserStatusUpdate(val toUid: String, val userUid: String, val status: User.Status) : MessagingEvent
}