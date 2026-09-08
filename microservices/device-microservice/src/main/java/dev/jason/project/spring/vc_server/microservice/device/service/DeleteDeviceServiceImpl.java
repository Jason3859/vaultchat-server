package dev.jason.project.spring.vc_server.microservice.device.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.service.DeleteDeviceService;
import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity;
import dev.jason.project.spring.vc_server.microservice.device.repo.DeviceRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DeleteDeviceServiceImpl implements DeleteDeviceService {

    protected final DeviceRepository deviceRepository;

    public DeleteDeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
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
