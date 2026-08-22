package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Message;

public interface MessagingService extends SendLogoutRequestService, InformUserStatusUpdateService {
	
	void sendMessage(Message message);
}
