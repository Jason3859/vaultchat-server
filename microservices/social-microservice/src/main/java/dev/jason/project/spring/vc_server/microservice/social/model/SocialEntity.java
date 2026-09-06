package dev.jason.project.spring.vc_server.microservice.social.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "social")
public record SocialEntity(
    @Id String userId,
    List<String> blocklist,
    List<String> connections
) {
}
