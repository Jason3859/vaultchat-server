package dev.jason.project.spring.vc_server.microservice.device.controller;

import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/device")
@AllArgsConstructor
public class DeviceController {

	private DeviceService deviceService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }
    

	@PostMapping("/add")
	public ResponseEntity<?> addDevice(@RequestBody DeviceDto device) {
		Device d = deviceService.addDevice(device.toDevice(LocalDateTime.now()));
		return new ResponseEntity<>(DeviceDto.asDto(d), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteDevice(@RequestParam String ownerUid, @RequestParam String deviceId) {
		deviceService.deleteDevice(ownerUid, deviceId);
	}
	
	@GetMapping("/mine")
	public ResponseEntity<List<DeviceDto>> getDevices(@RequestParam String uid) {
		List<DeviceDto> devices = deviceService.getDevicesByOwnerUid(uid)
			.stream()
			.map(DeviceDto::asDto)
			.toList();
		
		return new ResponseEntity<>(devices, HttpStatus.OK);
	}

    @GetMapping("/marked-for-logout")
    public List<Map<String, String>> devicesMarkedForLogout(@RequestParam String uid) {
        return deviceService.getDevicesMarkedForLogout(uid);
    }

	@DeleteMapping("/mark-logout")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void markDeviceForLogout(@RequestParam String deviceId, @RequestParam boolean clearMessages) {
		deviceService.markDeviceForLogout(deviceId, clearMessages);
	}
}
