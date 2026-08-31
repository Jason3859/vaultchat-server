package dev.jason.project.spring.vc_server.microservice.device.repo;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.microservice.device.model.LogoutDeviceDbEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceLogoutQueueRepository extends MongoRepository<LogoutDeviceDbEntity, Device> {
}
