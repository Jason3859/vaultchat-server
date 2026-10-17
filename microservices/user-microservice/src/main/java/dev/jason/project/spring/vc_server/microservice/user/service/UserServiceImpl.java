package dev.jason.project.spring.vc_server.microservice.user.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.exception.VcException.UserException.UserNotFoundException;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.service.UserService;
import dev.jason.project.spring.vc_server.microservice.user.model.UserEntity;
import dev.jason.project.spring.vc_server.microservice.user.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
        repository.setAllUsersOffline();
    }

    @Override
    public void addUser(User user) {
        Optional<UserEntity> optionalUserEntity = repository.findById(user.uid());
        if (optionalUserEntity.isPresent())
            throw new VcException.UserException.UserAlreadyExistsException();

        repository.save(UserEntity.asEntity(user, LocalDateTime.now()));
    }

    @Override
    public void deleteUser(String uid) {
        UserEntity entity = repository.findById(uid)
            .orElseThrow(UserNotFoundException::new);

        repository.delete(entity);
    }

    @Override
    public void setStatus(String uid, User.Status status) {
        UserEntity entity = repository.findById(uid)
            .orElseThrow(UserNotFoundException::new);

        entity.setStatus(status);
        repository.save(entity);
    }

    @Override
    public User updateDisplayName(String uid, String displayName) {
        UserEntity entity = repository.findById(uid)
            .orElseThrow(UserNotFoundException::new);

        entity.setDisplayName(displayName);
        return repository.save(entity).asUser();
    }

    @Override
    public List<User> getAllUsersByDisplayName(String fromUid, String searchQuery) {
        return repository.findByDisplayNameContainingIgnoreCase(searchQuery)
            .stream()
            .filter((entity) -> !fromUid.equalsIgnoreCase(entity.getUid()))
            .map(UserEntity::asUser)
            .toList();
    }

    @Override
    public User getUserByUid(String uid) {
        var entity = repository.findById(uid);
        if (entity.isEmpty()) throw new VcException.UserException.UserNotFoundException();
        return entity.get().asUser();
    }

    @Override
    public boolean doesUserNotExist(String uid) {
        try {
            getUserByUid(uid);
            return false;
        } catch (VcException.UserException.UserNotFoundException e) {
            return true;
        }
    }
    
    @Override
    public void updateLastSeenToNow(String uid) {
    	UserEntity entity = repository.findById(uid).orElseThrow(UserNotFoundException::new);
    	entity.setLastSeen(LocalDateTime.now());
    	repository.save(entity);
    }
}
