package dev.jason.project.spring.vc_server.microservice.user.model;

import dev.jason.project.spring.vc_server.core.model.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user")
public class UserEntity {

    @Id
    private final String uid;
    private String displayName;
    private final String profilePictureUrl;
    private User.Status status;

    public UserEntity(String uid, String displayName, String profilePictureUrl, User.Status status) {
        this.uid = uid;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.status = status;
    }

    public User asUser() {
        return new User(uid, displayName, profilePictureUrl, status);
    }

    public static UserEntity asEntity(User user) {
        return new UserEntity(user.uid(),
            user.displayName(),
            user.profilePictureUrl(),
            user.status());
    }
}
