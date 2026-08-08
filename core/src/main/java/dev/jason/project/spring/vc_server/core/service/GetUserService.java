package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.User;

public interface GetUserService {

    User getUserByUid(String uid);
}
