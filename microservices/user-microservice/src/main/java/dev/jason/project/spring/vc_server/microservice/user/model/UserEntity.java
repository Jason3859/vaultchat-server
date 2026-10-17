package dev.jason.project.spring.vc_server.microservice.user.model;

import dev.jason.project.spring.vc_server.core.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "user")
public class UserEntity {

    @Id
    private final String uid;
    private String displayName;
    private final String profilePictureUrl;
    private User.Status status;
    private LocalDateTime lastSeen;

    public User asUser() {
        return new User(uid, displayName, profilePictureUrl, status);
    }

    public static UserEntity asEntity(User user, LocalDateTime lastSeen) {
        return new UserEntity(user.uid(),
            user.displayName(),
            user.profilePictureUrl(),
            user.status(),
            lastSeen);
    }
}
