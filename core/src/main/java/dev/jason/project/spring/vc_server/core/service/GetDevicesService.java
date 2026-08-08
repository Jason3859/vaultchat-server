package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Device;

import java.util.List;

public interface GetDevicesService {

    List<Device> getDevicesByOwnerUid(String ownerUid);
}
