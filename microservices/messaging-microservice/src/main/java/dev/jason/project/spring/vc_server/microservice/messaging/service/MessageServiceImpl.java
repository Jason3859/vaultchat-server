package dev.jason.project.spring.vc_server.microservice.messaging.service;

import dev.jason.project.spring.vc_server.core.model.Message;
import dev.jason.project.spring.vc_server.core.service.GetUserService;
import dev.jason.project.spring.vc_server.core.service.MessageService;
import dev.jason.project.spring.vc_server.microservice.messaging.model.MessageEntity;
import dev.jason.project.spring.vc_server.microservice.messaging.repo.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

	private final MessageRepository messageRepository;
	private final GetUserService getUserService;
	
	@Override
	public void addMessage(Message message) {
		messageRepository.save(MessageEntity.fromMessage(message));
	}

	@Override
	public List<Message> getMessages(String uid) {
		getUserService.getUserByUid(uid);

		return messageRepository.findAllByFrom(uid).stream()
			.map(MessageEntity::toMessage)
			.toList();
	}
}
