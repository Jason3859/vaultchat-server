package dev.jason.project.spring.vc_server.microservice.user.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import dev.jason.project.spring.vc_server.microservice.user.model.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    List<UserEntity> findByDisplayNameContainingIgnoreCase(String displayName);

    @Query("{}")
    @Update("{ $set : { 'status' : 'Offline' }}")
    void setAllUsersOffline();
}
