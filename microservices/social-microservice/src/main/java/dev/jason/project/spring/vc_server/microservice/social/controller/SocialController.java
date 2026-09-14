package dev.jason.project.spring.vc_server.microservice.social.controller;

import dev.jason.project.spring.vc_server.core.dto.UserDto;
import dev.jason.project.spring.vc_server.core.service.SocialService;
import dev.jason.project.spring.vc_server.core.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/social")
public class SocialController {

    private final SocialService socialService;
    private final UserService userService;

    @GetMapping
    public String home() {
        return "Hello, World!";
    }

    @PostMapping("/register")
    public void register(@RequestParam String uid) {
        socialService.registerNewUser(uid);
    }

    @PatchMapping("/block")
    public ResponseEntity<?> blockUser(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
        socialService.block(fromUid, otherUid);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/unblock")
    public ResponseEntity<?> unblockUser(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
        socialService.unblock(fromUid, otherUid);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/connect")
    public ResponseEntity<?> connect(@RequestParam("from_uid") String fromUid, @RequestParam("other_uid") String otherUid) {
        socialService.tryConnect(fromUid, otherUid);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/connections")
    public ResponseEntity<List<UserDto>> connections(@RequestParam String uid) {
        List<UserDto> connections = socialService.getConnections(uid).stream()
            .map(userService::getUserByUid)
            .map(UserDto::fromUser)
            .toList();

        return new ResponseEntity<>(connections, HttpStatus.OK);
    }

    @GetMapping("/blocked-users")
    public ResponseEntity<List<UserDto>> blockedUsers(@RequestParam String uid) {
        List<UserDto> blockedUsers = socialService.getBlockedUsers(uid).stream()
            .map(userService::getUserByUid)
            .map(UserDto::fromUser)
            .toList();

        return new ResponseEntity<>(blockedUsers, HttpStatus.OK);
    }
}
