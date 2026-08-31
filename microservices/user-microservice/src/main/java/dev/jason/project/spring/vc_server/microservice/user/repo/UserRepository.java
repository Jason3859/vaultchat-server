package dev.jason.project.spring.vc_server.microservice.user.repo;

import dev.jason.project.spring.vc_server.microservice.user.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    List<UserEntity> findByDisplayNameContainingIgnoreCase(String displayName);
}
