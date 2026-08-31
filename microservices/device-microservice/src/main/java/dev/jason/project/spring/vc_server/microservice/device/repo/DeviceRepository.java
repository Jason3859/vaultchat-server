package dev.jason.project.spring.vc_server.microservice.device.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.jason.project.spring.vc_server.microservice.device.model.DeviceEntity;


@Repository
public interface DeviceRepository extends MongoRepository<DeviceEntity, String> {

	List<DeviceEntity> findByOwnerUid(String ownerUid);
}
