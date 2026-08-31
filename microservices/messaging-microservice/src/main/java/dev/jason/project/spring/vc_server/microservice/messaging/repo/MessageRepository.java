package dev.jason.project.spring.vc_server.microservice.messaging.repo;

import dev.jason.project.spring.vc_server.microservice.messaging.model.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {

	List<MessageEntity> findAllByFrom(String from);
}
