package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Message;

public interface MessageQueueService {

    void addMessageToQueue(String uid, Message message);
}
