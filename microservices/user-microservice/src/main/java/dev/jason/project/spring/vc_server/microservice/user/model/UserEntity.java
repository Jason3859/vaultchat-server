package dev.jason.project.spring.vc_server.microservice.user.model;

import dev.jason.project.spring.vc_server.core.model.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class UserEntity {

	@Id
	private final String uid;
	private String displayName;
	private String profilePictureUrl;
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
		return new User(uid, displayName, profilePictureUrl, status, lastHeartBeat);
	}

	public static UserEntity asEntity(User user) {
		return new UserEntity(user.uid(), user.displayName(), user.profilePictureUrl(), user.status(), user.lastHeartbeat());
	}

	public String getUid() {
		return this.uid;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public String getProfilePictureUrl() {
		return this.profilePictureUrl;
	}

	public User.Status getStatus() {
		return this.status;
	}

	public long getLastHeartBeat() {
		return this.lastHeartBeat;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void setProfilePictureUrl(String profilePictureUrl) {
		this.profilePictureUrl = profilePictureUrl;
	}

	public void setStatus(User.Status status) {
		this.status = status;
	}

	public void setLastHeartBeat(long lastHeartBeat) {
		this.lastHeartBeat = lastHeartBeat;
	}
}
