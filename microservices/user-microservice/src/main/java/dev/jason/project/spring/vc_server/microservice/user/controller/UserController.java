package dev.jason.project.spring.vc_server.microservice.user.controller;

import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.model.User;
import dev.jason.project.spring.vc_server.core.model.WsDestination;
import dev.jason.project.spring.vc_server.core.service.WebSocketMessagingService;
import dev.jason.project.spring.vc_server.core.service.SocialService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SocialService socialService;
    private final WebSocketMessagingService webSocketMessagingService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDto userDto) {
        userService.addUser(userDto.asNewUser());
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
        User user = userService.updateDisplayName(uid, displayName);

        socialService.getConnections(uid)
            .forEach(connectionUid -> {
                Object payload = Map.of("uid", uid, "new_display_name", user.displayName());
                webSocketMessagingService.send(WsDestination.TOPIC_CONNECTION_UPDATE, connectionUid, payload);
            });

        return user;
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> search(@RequestParam("from_uid") String fromUid, @RequestParam("search_query") String searchQuery) {

        if (searchQuery.isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userService.getUserByUid(fromUid); // for verification that user exists

        List<UserDto> requiredUsers = userService.getAllUsersByDisplayName(fromUid, searchQuery)
            .stream()
            .map(UserDto::fromUser)
            .toList();

        return new ResponseEntity<>(requiredUsers, HttpStatus.OK);
    }
}
