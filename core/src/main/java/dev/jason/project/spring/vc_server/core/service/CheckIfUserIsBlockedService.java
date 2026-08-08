package dev.jason.project.spring.vc_server.core.service;

public interface CheckIfUserIsBlockedService {

    boolean isUserBlocked(String uid1, String uid2);
}
