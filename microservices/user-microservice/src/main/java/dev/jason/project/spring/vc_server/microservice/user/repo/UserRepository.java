package dev.jason.project.spring.vc_server.microservice.user.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import dev.jason.project.spring.vc_server.microservice.user.model.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    List<UserEntity> findByDisplayNameContainingIgnoreCase(String displayName);
}
