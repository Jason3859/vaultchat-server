package dev.jason.project.spring.vc_server.microservice.social.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.service.SocialService;
import dev.jason.project.spring.vc_server.microservice.social.model.SocialEntity;
import dev.jason.project.spring.vc_server.microservice.social.repo.SocialRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final SocialRepository repository;

    @Override
    public List<String> getBlockedUsers(String uid) {
        return getSocialEntityByUid(uid).blocklist();
    }

    @Override
    public List<String> getConnections(String uid) {
        return getSocialEntityByUid(uid).connections();
    }

    @Override
    public boolean isUserBlocked(String uid1, String uid2) {
        Supplier<RuntimeException> exception = VcException.UserException.UserNotFoundException::new;

        var entity1 = repository.findByUserId(uid1).orElseThrow(exception);
        var entity2 = repository.findByUserId(uid2).orElseThrow(exception);

        return entity1.blocklist().contains(uid2) ||
            entity2.blocklist().contains(uid1);
    }

    @Override
    public void tryConnect(String uid1, String uid2) {
        Supplier<VcException> exceptionSupplier = VcException.UserException.UserNotFoundException::new;

        var entity1 = repository.findByUserId(uid1).orElseThrow(exceptionSupplier);
        var entity2 = repository.findByUserId(uid2).orElseThrow(exceptionSupplier);

        if (entity1.connections().contains(uid2)) { // if they are already connected
            return;
        }

        entity1.connections().add(uid2);
        entity2.connections().add(uid1);

        repository.save(entity1);
        repository.save(entity2);
    }

    @Override
    public void registerNewUser(String uid) {
        if (repository.findByUserId(uid).isPresent())
            throw new VcException.UserException.UserAlreadyExistsException();

        repository.save(new SocialEntity(uid, List.of(), List.of()));
    }

    @Override
    public void block(String uid1, String uid2) {
        if (uid1.equalsIgnoreCase(uid2))
            throw new VcException.SocialException.SelfBlockException();

        SocialEntity entity = getSocialEntityByUid(uid1);
        getSocialEntityByUid(uid2); // for verification that user exists

        boolean isBlocked = entity.blocklist().contains(uid2);
        if (isBlocked) throw new VcException.SocialException.UserAlreadyBlockedException();

        entity.blocklist().add(uid2);
        repository.save(entity);
    }

    @Override
    public void unblock(String uid1, String uid2) {
        if (uid1.equalsIgnoreCase(uid2))
            throw new VcException.SocialException.SelfUnblockException();

        SocialEntity entity = getSocialEntityByUid(uid1);
        getSocialEntityByUid(uid2); // for verification that user exists

        List<String> blocklist = entity.blocklist();
        boolean isBlocked = blocklist.contains(uid2);
        if (!isBlocked) throw new VcException.SocialException.UserNotBlockedException();

        blocklist.remove(uid2);
        repository.save(entity);
    }

    private SocialEntity getSocialEntityByUid(String uid) {
        return repository.findByUserId(uid).orElseThrow(VcException.UserException.UserNotFoundException::new);
    }
}
