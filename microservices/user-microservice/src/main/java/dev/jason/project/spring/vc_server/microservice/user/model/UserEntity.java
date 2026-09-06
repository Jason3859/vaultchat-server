package dev.jason.project.spring.vc_server.microservice.user.model;

import dev.jason.project.spring.vc_server.core.model.User;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "user")
public class UserEntity {

	@Id
	private final String uid;
	private String displayName;
	private final String profilePictureUrl;
	private User.Status status;
	private long lastHeartBeat;

	public UserEntity(String uid, String displayName, String profilePictureUrl, User.Status status, long lastHeartBeat) {
		this.uid = uid;
		this.displayName = displayName;
		this.profilePictureUrl = profilePictureUrl;
		this.status = status;
		this.lastHeartBeat = lastHeartBeat;
	}

	public User asUser() {
		return new User(uid, displayName, profilePictureUrl, status);
	}

	public static UserEntity asEntity(User user, long lastHeartBeat) {
		return new UserEntity(user.uid(), user.displayName(), user.profilePictureUrl().replace("=s96-c", ""), user.status(), lastHeartBeat);
	}

    public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setStatus(User.Status status) {
		this.status = status;
	}

	public void setLastHeartBeat(long lastHeartBeat) {
		this.lastHeartBeat = lastHeartBeat;
	}
}
