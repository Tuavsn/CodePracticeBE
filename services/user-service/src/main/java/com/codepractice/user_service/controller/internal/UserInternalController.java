package com.codepractice.user_service.controller.internal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.user_service.model.dto.internal.CreateUserRequest;
import com.codepractice.user_service.model.dto.internal.UpdateUserStatsRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInternalController {
  private final UserService userService;

  @PostMapping("/internal")
  public ResponseEntity<UserResponse> registNewUser(@RequestBody CreateUserRequest request) {
    UserResponse savedUser = userService.save(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(savedUser);
  }

  @PostMapping("/internal/update-stats")
  public ResponseEntity<Void> updateUserStats(@RequestBody UpdateUserStatsRequest request) {
    userService.updateUserStats(request);
    return ResponseEntity.noContent().build();
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
