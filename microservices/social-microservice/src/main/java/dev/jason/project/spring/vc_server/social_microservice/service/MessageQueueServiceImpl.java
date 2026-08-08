package dev.jason.project.spring.vc_server.social_microservice.service;

import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.service.MessageQueueService;
import dev.jason.project.spring.vc_server.social_microservice.repo.SocialRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class MessageQueueServiceImpl implements MessageQueueService {

    protected final SocialRepository repository;

    public MessageQueueServiceImpl(SocialRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addMessageToQueue(String uid, Message message) {
        var entity = repository.findByUserId(uid);

        if (entity.isEmpty()) throw new VcException.UserException.UserNotFoundException();
        entity.get().getQueuedMessages().add(message);
        repository.save(entity.get());
    }
}
