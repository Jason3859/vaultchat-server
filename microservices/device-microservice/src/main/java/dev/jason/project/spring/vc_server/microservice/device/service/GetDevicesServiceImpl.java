package dev.jason.project.spring.vc_server.microservice.device.service;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.service.GetDevicesService;
import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity;
import dev.jason.project.spring.vc_server.microservice.device.repo.DeviceRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class GetDevicesServiceImpl implements GetDevicesService {

    protected final DeviceRepository deviceRepository;

    public GetDevicesServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> getDevicesByOwnerUid(String ownerUid) {
        return deviceRepository.findByOwnerUid(ownerUid)
            .stream()
            .map(DeviceEntity::asDevice)
            .toList();
    }
}
