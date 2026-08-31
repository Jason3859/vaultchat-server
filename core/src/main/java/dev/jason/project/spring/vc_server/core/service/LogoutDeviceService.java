package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.LogoutDeviceEntity;
import jakarta.annotation.Nullable;

public interface LogoutDeviceService {

    @Nullable
    LogoutDeviceEntity getLogoutQueue(Device device);
    void logout(LogoutDeviceEntity entity);
    void removeFromLogoutQueue(Device device);
}
