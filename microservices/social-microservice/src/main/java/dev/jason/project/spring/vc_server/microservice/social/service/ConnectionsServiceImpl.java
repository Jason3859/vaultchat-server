package dev.jason.project.spring.vc_server.microservice.social.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.service.ConnectionsService;
import dev.jason.project.spring.vc_server.microservice.social.repo.SocialRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class ConnectionsServiceImpl implements ConnectionsService {

    protected final SocialRepository repository;

    public ConnectionsServiceImpl(SocialRepository repository) {
        this.repository = repository;
    }

    @Override
    public void connect(String uid1, String uid2) {
        var entity1 = repository.findByUserId(uid1);
        var entity2 = repository.findByUserId(uid2);

        if (entity1.isEmpty() || entity2.isEmpty()) {
            throw new VcException.UserException.UserNotFoundException();
        }

        if (entity1.get().getConnections().contains(uid2)) {
            return;
        }

        entity1.get().getConnections().add(uid2);
        entity2.get().getConnections().add(uid1);

        repository.save(entity1.get());
        repository.save(entity2.get());
    }

    @Override
    public List<String> getConnections(String uid) {
        var entity = repository.findByUserId(uid);

        if (entity.isEmpty()) throw new VcException.UserException.UserNotFoundException();
        return entity.get().getConnections();
    }
}
