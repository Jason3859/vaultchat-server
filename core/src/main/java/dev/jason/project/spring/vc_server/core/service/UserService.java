package dev.jason.project.spring.vc_server.core.service;

import java.time.LocalDateTime;
import java.util.List;

import dev.jason.project.spring.vc_server.core.model.User;

public interface UserService {

	void addUser(User user);
	void deleteUser(String uid);
	void setStatus(String uid, User.Status status);
	LocalDateTime updateLastSeenToNow(String uid);
	User updateDisplayName(String uid, String displayName);

	User getUserByUid(String uid);
	List<User> getAllUsersByDisplayName(String fromUid, String searchQuery);

	boolean doesUserNotExist(String uid);
}
