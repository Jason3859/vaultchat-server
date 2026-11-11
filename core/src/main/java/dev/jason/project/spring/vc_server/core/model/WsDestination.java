package dev.jason.project.spring.vc_server.core.model;

public enum WsDestination {
    TOPIC_MESSAGES, TOPIC_CONNECTION_UPDATE, TOPIC_CONNECTION_LAST_SEEN, TOPIC_LOGOUT_REQUEST;

    @Override
    public String toString() {
        return switch (this) {
            case TOPIC_MESSAGES -> "/topic/messages";
            case TOPIC_LOGOUT_REQUEST -> "/topic/logout-request";
            case TOPIC_CONNECTION_UPDATE -> "/topic/connection-update";
            case TOPIC_CONNECTION_LAST_SEEN -> "/topic/connection-last-seen";
        };
    }
}
