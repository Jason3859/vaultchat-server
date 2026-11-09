package dev.jason.project.spring.vc_server.core.model;

import java.time.LocalDateTime;

public record User(
    String uid,
    String displayName,
    String profilePictureUrl,
    Status status,
    LocalDateTime lastSeen
) {
    public enum Status {
        Online, Away, Offline
    }
    
    @Override
    public boolean equals(Object other) {
    	if (this == other) {
			return true;
		}
    	
    	if (!(other instanceof User)) {
			return false;
		}
    	
    	return this.uid().equals(((User) other).uid());
    }
}
