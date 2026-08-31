package dev.jason.project.spring.vc_server.microservice.device.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.LogoutDeviceEntity;
import dev.jason.project.spring.vc_server.core.service.GetDevicesService;
import dev.jason.project.spring.vc_server.core.service.LogoutDeviceService;
import dev.jason.project.spring.vc_server.microservice.device.model.LogoutDeviceDbEntity;
import dev.jason.project.spring.vc_server.microservice.device.repo.DeviceLogoutQueueRepository;
import jakarta.annotation.Nullable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class LogoutDeviceServiceImpl implements LogoutDeviceService {

    protected final DeviceLogoutQueueRepository deviceLogoutQueueRepository;
    protected final GetDevicesService getDevicesService;

    public LogoutDeviceServiceImpl(DeviceLogoutQueueRepository deviceLogoutQueueRepository, GetDevicesService getDevicesService) {
        this.deviceLogoutQueueRepository = deviceLogoutQueueRepository;
        this.getDevicesService = getDevicesService;
    }

    @Override
    @Nullable
    public LogoutDeviceEntity getLogoutQueue(Device device) {
        return deviceLogoutQueueRepository.findById(device)
            .map(LogoutDeviceDbEntity::toLogoutDeviceDbEntity)
            .orElse(null);
    }

    @Override
    public void logout(LogoutDeviceEntity entity) {
        var devices = getDevicesService.getDevicesByOwnerUid(entity.device().ownerUid());
        if (!devices.contains(entity.device())) throw new VcException.DeviceException.DeviceNotFoundException();
        deviceLogoutQueueRepository.save(LogoutDeviceDbEntity.fromLogoutDeviceEntity(entity));
    }

    @Override
    public void removeFromLogoutQueue(Device device) {
        var entity = deviceLogoutQueueRepository.findById(device);
        if (entity.isEmpty()) throw new VcException.DeviceException.NotInLogoutQueueException();
        deviceLogoutQueueRepository.deleteById(device);
    }
}
