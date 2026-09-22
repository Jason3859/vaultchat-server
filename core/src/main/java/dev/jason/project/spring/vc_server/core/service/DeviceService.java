package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Device;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceService {

    List<Device> getDevicesByOwnerUid(String ownerUid);

    Device addDevice(Device device);
    void deleteDevice(String ownerUid, String id);

    boolean doesDeviceExist(String deviceId);

    void updateLastUsed(String deviceId, LocalDateTime lastUsed);
    void markDeviceForLogout(String deviceId, boolean clearMessages);
}
