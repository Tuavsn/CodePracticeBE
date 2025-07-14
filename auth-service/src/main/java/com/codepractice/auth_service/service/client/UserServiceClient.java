package com.codepractice.auth_service.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codepractice.auth_service.model.dto.external.UserRequest;
import com.codepractice.auth_service.model.dto.external.UserResponse;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/users/internal/{id}")
    UserResponse getUserById(@PathVariable Long id);

    @GetMapping("/users/internal/email")
    UserResponse getUserByEmail(@RequestParam("email") String email);

    @PostMapping("/users/internal")
    UserResponse createUser(UserRequest user);
}
