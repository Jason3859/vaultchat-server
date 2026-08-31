package dev.jason.project.spring.vc_server.microservice.device.model;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.LogoutDeviceEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("device_logout_queue")
public record LogoutDeviceDbEntity(@Id Device device, boolean clearMessages) {

    public static LogoutDeviceDbEntity fromLogoutDeviceEntity(LogoutDeviceEntity entity) {
        return new LogoutDeviceDbEntity(entity.device(), entity.clearMessages());
    }

    public LogoutDeviceEntity toLogoutDeviceDbEntity() {
        return new LogoutDeviceEntity(device, clearMessages);
    }
}
