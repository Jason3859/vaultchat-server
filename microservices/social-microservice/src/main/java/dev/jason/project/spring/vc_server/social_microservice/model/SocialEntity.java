package dev.jason.project.spring.vc_server.social_microservice.model;

import dev.jason.project.spring.vc_server.core.model.Message;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "social")
public class SocialEntity {

	@Id
	private final String userId;
	private final List<String> blocklist;
	private final List<String> connections;
	private final List<Message> queuedMessages;

	public SocialEntity(String userId, List<String> blocklist, List<String> connections, List<Message> queuedMessages) {
		this.userId = userId;
		this.blocklist = blocklist;
		this.connections = connections;
		this.queuedMessages = queuedMessages;
	}

	public String getUserId() {
		return this.userId;
	}

	public List<String> getBlocklist() {
		return this.blocklist;
	}

	public List<String> getConnections() {
		return this.connections;
	}

	public List<Message> getQueuedMessages() {
		return this.queuedMessages;
	}
}
