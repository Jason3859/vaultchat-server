package dev.jason.project.spring.vc_server.core.dto;

import java.time.LocalDateTime;

import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.model.User.Status;

public record UserDto(String uid, String displayName, String profilePictureUrl, Status status, LocalDateTime lastSeen) {

	public static UserDto fromUser(User user) {
		return new UserDto(user.uid(), user.displayName(), user.profilePictureUrl(), user.status(), user.lastSeen());
	}
	
	public User asNewUser() {
		return new User(uid, displayName, profilePictureUrl, status, LocalDateTime.now());
	}
}