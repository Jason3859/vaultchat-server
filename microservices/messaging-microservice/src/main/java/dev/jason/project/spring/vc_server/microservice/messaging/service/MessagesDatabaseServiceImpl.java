package dev.jason.project.spring.vc_server.microservice.messaging.service;

import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.service.MessagesDatabaseService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import dev.jason.project.spring.vc_server.microservice.messaging.model.MessageEntity;
import dev.jason.project.spring.vc_server.microservice.messaging.repo.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MessagesDatabaseServiceImpl implements MessagesDatabaseService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    @Override
    public Message addMessage(Message message) {
        MessageEntity messageEntity = messageRepository.save(MessageEntity.fromMessage(message));
        return asMessage(messageEntity);
    }

    @Override
    public List<Message> getMessages(String uid) {
        userService.getUserByUid(uid);

        var sent = messageRepository.findAllByFrom(uid);
        var received = messageRepository.findAllByTo(uid);

        var messages = new ArrayList<>(sent);
        messages.addAll(received);

        return messages.stream()
            .map(this::asMessage)
            .toList();
    }

    private Message asMessage(MessageEntity entity) {
        var from = userService.getUserByUid(entity.from());
        var to = userService.getUserByUid(entity.to());

        return new Message(entity.id(), from, to, entity.text(), entity.timestamp());
    }
}
