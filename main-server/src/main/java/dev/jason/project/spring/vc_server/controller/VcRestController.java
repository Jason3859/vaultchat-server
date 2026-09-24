package dev.jason.project.spring.vc_server.controller;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.WsDestination;
import dev.jason.project.spring.vc_server.core.service.DeviceService;
import dev.jason.project.spring.vc_server.core.service.SocialService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import dev.jason.project.spring.vc_server.core.service.WebSocketMessagingService;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@AllArgsConstructor
public class VcRestController {

    private UserService userService;
    private DeviceService deviceService;
    private SocialService socialService;
    private WebSocketMessagingService webSocketMessagingService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserDto registerUser(@RequestBody RegisterUserDto dto) {

        try {
            userService.addUser(dto.asUserDto().asUser());
            socialService.registerNewUser(dto.uid());
        } catch (VcException.UserException.UserAlreadyExistsException _) {
            // ignore this and continue adding the new device to db
        }

        var device = deviceService.addDevice(dto.asDeviceDto().toDevice(LocalDateTime.now()));
        return dto.withDevice(DeviceDto.asDto(device));
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> logout(@RequestParam String uid, @RequestParam String deviceId) {
        deviceService.deleteDevice(uid, deviceId);
        return Map.of("uid", uid, "device_id", deviceId);
    }

    @DeleteMapping("/request-logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, ?> requestLogout(@RequestParam String deviceId, @RequestParam("clear_messages") boolean clearMessages) {
		Device device = deviceService.markDeviceForLogout(deviceId, clearMessages);
        webSocketMessagingService.send(WsDestination.TOPIC_LOGOUT_REQUEST, device.ownerUid(), Map.of("deviceId", device.id(), "clear_messages", clearMessages));
        return Map.of("device_id", deviceId, "clear_messages", clearMessages);
    }
}
