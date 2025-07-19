package com.codepractice.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.user_service.model.dto.request.UserRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInternalController {
    private final UserService userService;

    @PostMapping("/internal")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest user) {
        UserResponse savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @PutMapping("/internal")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest user) {
        UserResponse updatedUser = userService.update(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        UserResponse user = userService.getById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
