package dev.jason.project.spring.vc_server.microservice.user.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.service.GetUserService;
import dev.jason.project.spring.vc_server.microservice.user.repo.UserRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class GetUserServiceImpl implements GetUserService {

    protected final UserRepository repository;

    public GetUserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getUserByUid(String uid) {
        var entity = repository.findById(uid);
        if (entity.isEmpty()) throw new VcException.UserException.UserNotFoundException();
        return entity.get().asUser();
    }

    @Override
    public boolean doesUserExist(String uid) {
        try {
            getUserByUid(uid);
            return true;
        } catch (VcException.UserException.UserNotFoundException e) {
            return false;
        }
    }
}
