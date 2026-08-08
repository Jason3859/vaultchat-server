package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.User;

public interface InformUserStatusUpdateService {

    void sendStatusUpdate(String uid, User.Status status);
}
