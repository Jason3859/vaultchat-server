package dev.jason.project.spring.vc_server.dto;

import java.time.LocalDateTime;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.Device.OS;
import dev.jason.project.spring.vc_server.core.model.Device.Type;
import dev.jason.project.spring.vc_server.core.model.User.Status;

public record RegisterUserDto(
    String uid,
    String displayName,
    String profilePictureUrl,
    Device device
) {
    public record Device(
        String id,
        String name,
        Type type,
        OS os,
        String version
    ) {
    }

    public UserDto asUserDto() {
        return new UserDto(uid, displayName, profilePictureUrl, Status.Online, LocalDateTime.now());
    }

    public DeviceDto asDeviceDto() {
        return new DeviceDto(device.id(), uid, device.name(), device.type(), device.os(), device.version());
    }

    public RegisterUserDto withDevice(DeviceDto device) {
        return new RegisterUserDto(uid,
            displayName,
            profilePictureUrl,
            new Device(device.id(), device.name(), device.type(), device.os(), device.version()));
    }
}
