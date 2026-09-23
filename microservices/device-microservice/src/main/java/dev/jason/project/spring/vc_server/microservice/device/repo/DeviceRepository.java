package dev.jason.project.spring.vc_server.microservice.device.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity;

public interface DeviceRepository extends MongoRepository<DeviceEntity, String> {

	List<DeviceEntity> findByOwnerUid(String ownerUid);
    List<DeviceEntity> findByOwnerUidAndLogoutMarkIsMarkedTrue(String ownerUid);
}
