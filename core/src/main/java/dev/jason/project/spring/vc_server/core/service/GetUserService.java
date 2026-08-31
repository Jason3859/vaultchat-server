package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException;
import dev.jason.project.spring.vc_server.core.model.User;

public interface GetUserService {

    /**
     * @throws UserNotFoundException if user does not exist
     * @return User instance of user if that user exists in database
     */
    User getUserByUid(String uid);
}
