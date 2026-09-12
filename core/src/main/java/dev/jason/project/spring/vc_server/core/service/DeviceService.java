package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Device;

import java.time.LocalDateTime;

public interface DeviceService extends GetDevicesService, DeleteDeviceService {

    Device addDevice(Device device);
    boolean doesDeviceExist(String deviceId);
    void updateLastUsed(String deviceId, LocalDateTime lastUsed);
    void markDeviceForLogout(Device device, boolean clearMessages);
}
