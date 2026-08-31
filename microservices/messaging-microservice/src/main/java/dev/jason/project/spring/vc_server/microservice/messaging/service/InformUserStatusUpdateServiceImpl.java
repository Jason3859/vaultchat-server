package dev.jason.project.spring.vc_server.microservice.messaging.service;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.service.InformUserStatusUpdateService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class InformUserStatusUpdateServiceImpl implements InformUserStatusUpdateService {

    @Override
    public void sendStatusUpdate(String uid, User.Status status) {
        // TODO
    }
}
