package dev.jason.project.spring.vc_server.microservice.social.repo;

import dev.jason.project.spring.vc_server.microservice.social.model.SocialEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialRepository extends MongoRepository<SocialEntity, String> {

	Optional<SocialEntity> findByUserId(String userId);
}
