package dev.jason.project.spring.vc_server.core.service;

import java.util.List;

public interface SocialService {

	List<String> getBlockedUsers(String uid);
	List<String> getConnections(String uid);
	boolean isUserBlocked(String uid1, String uid2);

	void connect(String uid1, String uid2);
	void registerNewUser(String uid);
	void block(String uid1, String uid2);
	void unblock(String uid1, String uid2);
}
