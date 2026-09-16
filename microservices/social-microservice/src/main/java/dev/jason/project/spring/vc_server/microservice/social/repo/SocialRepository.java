package dev.jason.project.spring.vc_server.microservice.social.repo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import dev.jason.project.spring.vc_server.microservice.social.model.SocialEntity;

public interface SocialRepository extends MongoRepository<SocialEntity, String> {

	Optional<SocialEntity> findByUserId(String userId);
}
