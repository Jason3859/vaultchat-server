package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.User;

import java.util.List;

public interface UserService extends GetUserService {

	void addUser(User user);
	void deleteUser(String uid);
	void updateHeartBeat(String uid);
	
	List<User> getAllUsersByDisplayName(String query);
	List<User> searchUsers(String fromUid, String searchQuery);
}
