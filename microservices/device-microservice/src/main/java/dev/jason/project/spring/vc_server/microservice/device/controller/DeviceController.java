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
	public ResponseEntity<?> deleteDevice(@RequestBody DeviceDto device) {
		deviceService.deleteDevice(device.toDevice());
		return new ResponseEntity<>(device, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/mine")
	public ResponseEntity<List<DeviceDto>> getDevices(@RequestParam String uid) {
		List<DeviceDto> devices = deviceService.getDevicesByOwnerUid(uid)
			.stream()
			.map(DeviceDto::asDto)
			.toList();
		
		return new ResponseEntity<>(devices, HttpStatus.OK);
	}

	@DeleteMapping("/mark-logout")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void markDeviceForLogout(@RequestBody DeviceDto deviceDto, @RequestParam boolean clearMessages) {
		deviceService.markDeviceForLogout(deviceDto.toDevice(), clearMessages);
	}

	@DeleteMapping("/logout")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void logoutDevice(@RequestBody DeviceDto deviceDto) {
		deviceService.deleteDevice(deviceDto.toDevice());
	}
}
