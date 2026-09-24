package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Device;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DeviceService {

    List<Device> getDevicesByOwnerUid(String ownerUid);
    List<Map<String, String>> getDevicesMarkedForLogout(String ownerUid);

    Device addDevice(Device device);
    void deleteDevice(String ownerUid, String id);

    boolean doesDeviceExist(String deviceId);

    void updateLastUsed(String deviceId, LocalDateTime lastUsed);
    Device markDeviceForLogout(String deviceId, boolean clearMessages);
}
