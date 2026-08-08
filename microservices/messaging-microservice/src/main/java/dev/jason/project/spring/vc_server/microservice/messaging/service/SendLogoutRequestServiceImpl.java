package dev.jason.project.spring.vc_server.microservice.messaging.service;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.service.SendLogoutRequestService;
import dev.jason.project.spring.vc_server.microservice.messaging.repo.messaging.MessagingRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class SendLogoutRequestServiceImpl implements SendLogoutRequestService {

    protected final MessagingRepository messagingRepository;

    public SendLogoutRequestServiceImpl(MessagingRepository messagingRepository) {
        this.messagingRepository = messagingRepository;
    }

    @Override
    public void sendLogoutRequest(Device device, boolean clearMessages) {
        messagingRepository.sendLogoutRequest(device, clearMessages);
    }
}
