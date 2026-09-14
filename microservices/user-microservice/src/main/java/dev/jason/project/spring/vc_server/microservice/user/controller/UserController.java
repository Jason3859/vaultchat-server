package dev.jason.project.spring.vc_server.microservice.user.controller;

import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDto userDto) {
		userService.addUser(userDto.asUser());
		return new ResponseEntity<>(userDto, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/delete")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteUser(@RequestParam String uid) {
		userService.deleteUser(uid);
	}

	@PatchMapping("/update-displayname")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public User updateDisplayName(@RequestParam String uid, @RequestParam String displayName) {
		return userService.updateDisplayName(uid, displayName);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<UserDto>> search(@RequestParam("from_uid") String fromUid, @RequestParam("search_query") String searchQuery) {
		
		if (searchQuery.isBlank()) {
			return new ResponseEntity<>(List.of(), HttpStatus.BAD_REQUEST);
		}
		
		userService.getUserByUid(fromUid); // for verification that user exists
		
		List<UserDto> requiredUsers = userService.getAllUsersByDisplayName(fromUid, searchQuery)
			.stream()
			.map(UserDto::fromUser)
			.toList();
		
		return new ResponseEntity<>(requiredUsers, HttpStatus.OK);
	}
}
