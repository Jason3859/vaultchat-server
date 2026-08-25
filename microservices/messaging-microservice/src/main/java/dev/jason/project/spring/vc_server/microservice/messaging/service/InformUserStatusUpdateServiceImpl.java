package dev.jason.project.spring.vc_server.microservice.messaging.service;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.service.ConnectionsService;
import dev.jason.project.spring.vc_server.core.service.GetDevicesService;
import dev.jason.project.spring.vc_server.core.service.InformUserStatusUpdateService;
import dev.jason.project.spring.vc_server.microservice.messaging.repo.MessagingRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class InformUserStatusUpdateServiceImpl implements InformUserStatusUpdateService {

    protected final MessagingRepository repository;
    protected final ConnectionsService connectionsService;
    protected final GetDevicesService getDevicesService;

    public InformUserStatusUpdateServiceImpl(MessagingRepository repository, ConnectionsService connectionsService, GetDevicesService getDevicesService) {
        this.repository = repository;
        this.connectionsService = connectionsService;
        this.getDevicesService = getDevicesService;
    }

    @Override
    public void sendStatusUpdate(String uid, User.Status status) {
        List<String> connections = connectionsService.getConnections(uid);

        connections.forEach(connectionUid -> {
            List<Device> devices = getDevicesService.getDevicesByOwnerUid(connectionUid);

            if (devices.isEmpty()) {
                return;
            }

            devices.forEach(device -> {
                repository.sendUserStatusUpdate(device, uid, status);
            });
        });
    }
}
