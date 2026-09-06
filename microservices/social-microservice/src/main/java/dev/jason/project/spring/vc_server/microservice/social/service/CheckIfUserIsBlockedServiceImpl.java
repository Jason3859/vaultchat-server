package dev.jason.project.spring.vc_server.microservice.social.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.service.CheckIfUserIsBlockedService;
import dev.jason.project.spring.vc_server.microservice.social.repo.SocialRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@Primary
public class CheckIfUserIsBlockedServiceImpl implements CheckIfUserIsBlockedService {

    protected final SocialRepository repository;

    public CheckIfUserIsBlockedServiceImpl(SocialRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isUserBlocked(String uid1, String uid2) {
        Supplier<RuntimeException> exception = VcException.UserException.UserNotFoundException::new;

        var entity1 = repository.findByUserId(uid1).orElseThrow(exception);
        var entity2 = repository.findByUserId(uid2).orElseThrow(exception);

        return entity1.blocklist().contains(uid2) ||
            entity2.blocklist().contains(uid1);
    }
}
