package dev.jason.project.spring.vc_server.microservice.device.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceNotFoundException;
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.service.DeviceService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity;
import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity.LogoutMark;
import dev.jason.project.spring.vc_server.microservice.device.repo.DeviceRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final UserService userService;

    @Override
    public Device addDevice(Device device) {
        if (userService.doesUserNotExist(device.ownerUid()))
            throw new UserNotFoundException();

        DeviceEntity entity = DeviceEntity.asEntity(device);
        return deviceRepository.save(entity).asDevice();
    }

    @Override
    public boolean doesDeviceExist(String deviceId) {
        return deviceRepository.existsById(deviceId);
    }

    @Override
    public void markDeviceForLogout(Device device, boolean clearMessages) {
        DeviceEntity entity = deviceRepository.findById(device.id())
            .orElseThrow(DeviceNotFoundException::new);

        LogoutMark logoutMark = new LogoutMark(true, clearMessages);
        entity.setLogoutMark(logoutMark);
        deviceRepository.save(entity);
    }

    @Override
    public void updateLastUsed(String deviceId, LocalDateTime lastUsed) {
        DeviceEntity entity = deviceRepository.findById(deviceId)
            .orElseThrow(DeviceNotFoundException::new);

        entity.setLastUsed(lastUsed);
        deviceRepository.save(entity);
    }

    @Override
    public List<Device> getDevicesByOwnerUid(String ownerUid) {
        List<DeviceEntity> devices = deviceRepository.findByOwnerUid(ownerUid);
        return devices.stream()
            .map(DeviceEntity::asDevice)
            .toList();
    }

    @Override
    public void deleteDevice(Device device) {
        DeviceEntity entity = deviceRepository.findByOwnerUidAndName(device.ownerUid(), device.name());

        if (entity != null) {
            deviceRepository.delete(entity);
            return;
        }

        throw new VcException.DeviceException.DeviceNotFoundException();
    }
}
