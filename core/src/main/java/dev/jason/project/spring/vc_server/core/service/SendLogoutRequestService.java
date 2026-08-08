package dev.jason.project.spring.vc_server.core.service;

import dev.jason.project.spring.vc_server.core.model.Device;

public interface SendLogoutRequestService {

    void sendLogoutRequest(Device device, boolean clearMessages);
}
