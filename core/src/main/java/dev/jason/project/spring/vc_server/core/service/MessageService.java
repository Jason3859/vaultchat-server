package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Message;

import java.util.List;

public interface MessageService {

	Message addMessage(Message message);
	List<Message> getMessages(String uid);
}
