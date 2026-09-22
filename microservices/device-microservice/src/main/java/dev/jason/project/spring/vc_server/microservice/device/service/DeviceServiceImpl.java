package dev.jason.project.spring.vc_server.microservice.device.service;

import dev.jason.project.spring.vc_server.core.exception.VcException.DeviceException.DeviceNotFoundException;
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.service.DeviceService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity;
import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity.LogoutMark;
import dev.jason.project.spring.vc_server.microservice.device.repo.DeviceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public void markDeviceForLogout(String deviceId, boolean clearMessages) {
        DeviceEntity entity = deviceRepository.findById(deviceId)
            .orElseThrow(DeviceNotFoundException::new);

        LogoutMark logoutMark = LogoutMark.getInstance(clearMessages);
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
    public void deleteDevice(String ownerUid, String id) {
        DeviceEntity entity = deviceRepository.findById(id)
            .orElseThrow(DeviceNotFoundException::new);

        if (ownerUid.equals(entity.getOwnerUid())) {
            deviceRepository.delete(entity);
        }
    }
}
