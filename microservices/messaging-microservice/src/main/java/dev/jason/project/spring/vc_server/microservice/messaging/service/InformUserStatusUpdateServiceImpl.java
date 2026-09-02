package dev.jason.project.spring.vc_server.microservice.messaging.service;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.service.ConnectionsService;
import dev.jason.project.spring.vc_server.core.service.InformUserStatusUpdateService;
import dev.jason.project.spring.vc_server.microservice.messaging.events.MessagingEvent;
import dev.jason.project.spring.vc_server.microservice.messaging.events.MessagingEvents;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@AllArgsConstructor
public class InformUserStatusUpdateServiceImpl implements InformUserStatusUpdateService {

    private ConnectionsService connectionsService;

    @Override
    public void sendStatusUpdate(String userUid, User.Status userStatus) {
        connectionsService.getConnections(userUid).forEach(uid -> {
            MessagingEvents.sendEvent(new MessagingEvent.UserStatusUpdate(uid, userUid, userStatus));
        });
    }
}
