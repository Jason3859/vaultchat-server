package dev.jason.project.spring.vc_server.controller;

import dev.jason.project.spring.vc_server.core.Endpoints;
import dev.jason.project.spring.vc_server.core.dto.DeviceDto;
import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.user_microservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Endpoints.USER)
public class UserController {
	
	@Autowired
	private UserService userService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

	@PostMapping(Endpoints.REGISTER)
	public ResponseEntity<?> register(@RequestBody UserDto userDto) {
		userService.addUser(userDto.asUser());
		return new ResponseEntity<>(userDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping(Endpoints.DELETE)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteUser(@RequestParam String uid) {
		userService.deleteUser(uid);
	}
	
	@PatchMapping(Endpoints.HEARTBEAT)
	public void heartbeat(@RequestParam String uid) {
		userService.updateHeartBeat(uid);
	}
	
	@GetMapping(Endpoints.GET_USER_BY_UID)
	public UserDto getUserById(@RequestParam String uid) {
		User user = userService.getUserByUid(uid);
		return UserDto.fromUser(user);
	}
	
	@GetMapping(Endpoints.SEARCH)
	public ResponseEntity<List<UserDto>> search(@RequestParam("from_uid") String fromUid, @RequestParam("search_query") String searchQuery) {
		
		if (searchQuery.isBlank()) {
			return new ResponseEntity<>(List.of(), HttpStatus.BAD_REQUEST);
		}
		
		userService.getUserByUid(fromUid); // for verification that user exists
		
		List<UserDto> requiredUsers = userService.searchUsers(fromUid, searchQuery)
			.stream()
			.map(UserDto::fromUser)
			.toList();
		
		return new ResponseEntity<>(requiredUsers, HttpStatus.OK);
	}
	
	@DeleteMapping(Endpoints.LOGOUT)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public DeviceDto logout(@RequestParam boolean clearMessages, @RequestBody DeviceDto device) {
		userService.logout(device.toDevice(null), clearMessages);
		return device;
	}
}
