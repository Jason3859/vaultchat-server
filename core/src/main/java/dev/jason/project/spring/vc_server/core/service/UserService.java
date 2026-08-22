package dev.jason.project.spring.vc_server.core.service;

import java.util.List;

import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.User;

public interface UserService extends GetUserService {

	void addUser(User user);
	void deleteUser(String uid);
	void updateHeartBeat(String uid);
	void logout(Device device, boolean clearMessages);
	
	List<User> getAllUsersByDisplayName(String query);
	List<User> searchUsers(String fromUid, String searchQuery);
}
