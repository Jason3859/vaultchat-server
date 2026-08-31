package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Device;

public interface DeviceService extends GetDevicesService, DeleteDeviceService, LogoutDeviceService {

    Device addDevice(Device device);
    void verifyDevice(Device device);
}
