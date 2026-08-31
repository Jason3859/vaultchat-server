package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.User;

import java.util.List;

public interface SocialService extends CheckIfUserIsBlockedService, ConnectionsService, MessageQueueService {

	List<User> getBlockedUsers(String uid);
	
	void registerNewUser(String uid);
	void block(String uid1, String uid2);
	void unblock(String uid1, String uid2);
}
