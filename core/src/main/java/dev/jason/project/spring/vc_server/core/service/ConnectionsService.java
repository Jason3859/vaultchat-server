package dev.jason.project.spring.vc_server.core.service;

import java.util.List;

public interface ConnectionsService {

    void connect(String uid1, String uid2);
    List<String> getConnections(String uid);
}
