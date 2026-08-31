package dev.jason.project.spring.vc_server.core.model;

import java.time.LocalDateTime;

public record Device(String ownerUid, String name, Type type, OS os, String version, String token, LocalDateTime lastUsed) {
    public enum OS {
        Android // currently only android is supported. may add support to other oses in the future.
    }

    public enum Type {
        Mobile, Tablet // currently only mobile and tablet are supported. may add support to other devices in the future.
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Device other)) return false;

        return ownerUid.equals(other.ownerUid) &&
            name.equals(other.name) &&
            token.equals(other.token);
    }
}
