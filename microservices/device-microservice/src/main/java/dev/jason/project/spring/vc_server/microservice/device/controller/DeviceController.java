package dev.jason.project.spring.vc_server.microservice.device.controller;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.model.Device;
import dev.jason.project.spring.vc_server.core.model.LogoutDeviceEntity;
import dev.jason.project.spring.vc_server.core.service.DeviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(Endpoints.DEVICE)
@AllArgsConstructor
public class DeviceController {

	private DeviceService deviceService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }
    

	@PostMapping(Endpoints.ADD)
	public ResponseEntity<?> addDevice(@RequestBody DeviceDto device) {
		Device d = deviceService.addDevice(device.toDevice(LocalDateTime.now()));
		return new ResponseEntity<>(DeviceDto.asDto(d), HttpStatus.CREATED);
	}
	
	@DeleteMapping(Endpoints.DELETE)
	public ResponseEntity<?> deleteDevice(@RequestBody DeviceDto device) {
		deviceService.deleteDevice(device.toDevice());
		return new ResponseEntity<>(device, HttpStatus.ACCEPTED);
	}
	
	@GetMapping(Endpoints.MY_DEVICES)
	public ResponseEntity<List<DeviceDto>> getDevices(@RequestParam String uid) {
		List<DeviceDto> devices = deviceService.getDevicesByOwnerUid(uid)
			.stream()
			.map(DeviceDto::asDto)
			.toList();
		
		return new ResponseEntity<>(devices, HttpStatus.OK);
	}

	@DeleteMapping(Endpoints.LOGOUT)
	public void logoutDevice(@RequestBody DeviceDto deviceDto, @RequestParam boolean clearMessages) {
		deviceService.logout(new LogoutDeviceEntity(deviceDto.toDevice(), clearMessages));
	}

	@PostMapping(Endpoints.LOGOUT_ACKNOWLEDGEMENT)
	public void logoutAcknowledgement(@RequestBody DeviceDto deviceDto) {
		deviceService.removeFromLogoutQueue(deviceDto.toDevice());
	}

	@GetMapping(Endpoints.LOGOUT_QUEUE)
	public LogoutDeviceEntity isInLogoutQueue(@RequestBody DeviceDto deviceDto) {
		return deviceService.getLogoutQueue(deviceDto.toDevice());
	}
}
