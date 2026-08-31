package dev.jason.project.spring.vc_server.core.dto;

import dev.jason.project.spring.vc_server.core.model.Device;

import java.time.LocalDateTime;

public record DeviceDto(String ownerUid, String name, Device.Type type, Device.OS os, String version, String token) {

    public Device toDevice() { return toDevice(null); }
    public Device toDevice(LocalDateTime lastTimeUsed) {
        return new Device(ownerUid, name, type, os, version, token, lastTimeUsed);
    }

    public static DeviceDto asDto(Device device) {
        return new DeviceDto(device.ownerUid(), device.name(), device.type(), device.os(), device.version(), device.token());
    }
}
