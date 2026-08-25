package dev.jason.project.spring.vc_server.microservice.social.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.service.CheckIfUserIsBlockedService;
import dev.jason.project.spring.vc_server.microservice.social.repo.SocialRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CheckIfUserIsBlockedServiceImpl implements CheckIfUserIsBlockedService {

    protected final SocialRepository repository;

    public CheckIfUserIsBlockedServiceImpl(SocialRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isUserBlocked(String uid1, String uid2) {
        var entity1 = repository.findByUserId(uid1);
        var entity2 = repository.findByUserId(uid2);

        if (entity1.isEmpty() || entity2.isEmpty()) throw new VcException.UserException.UserNotFoundException();

        return entity1.get().getBlocklist().contains(uid2) ||
            entity2.get().getBlocklist().contains(uid1);
    }
}
