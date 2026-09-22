package dev.jason.project.spring.vc_server.controller;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.exception.VcException;
import dev.jason.project.spring.vc_server.dto.RegisterUserDto;
import dev.jason.project.spring.vc_server.microservice.device.controller.DeviceController;
import dev.jason.project.spring.vc_server.microservice.social.controller.SocialController;
import dev.jason.project.spring.vc_server.microservice.user.controller.UserController;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
public class VcRestController {

    private UserController userController;
    private DeviceController deviceController;
    private SocialController socialController;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserDto registerUser(@RequestBody RegisterUserDto dto) {

        try {
            userController.register(dto.asUserDto());
            socialController.register(dto.uid());
        } catch (VcException.UserException.UserAlreadyExistsException _) {
            // ignore this and continue adding the new device to db
        }

        var response = deviceController.addDevice(dto.asDeviceDto());
        var body = response.getBody();
        var device = (DeviceDto) body;
        assert device != null;
        return dto.withDevice(device);
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, String> logout(@RequestParam String uid, @RequestParam String deviceId) {
        deviceController.deleteDevice(uid, deviceId);
        return Map.of("uid", uid, "device_id", deviceId);
    }

    @DeleteMapping("/request-logout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Map<String, ?> requestLogout(@RequestParam String deviceId, @RequestParam("clear_messages") boolean clearMessages) {
		deviceController.markDeviceForLogout(deviceId, clearMessages);
        return Map.of("device_id", deviceId, "clear_messages", clearMessages);
    }
}
