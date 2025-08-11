package com.codepractice.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codepractice.auth_service.model.dto.internal.UserClientRequest;
import com.codepractice.auth_service.model.dto.internal.UserClientResponse;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/users/internal/{id}")
    UserClientResponse getUserById(@PathVariable Long id);

    @PostMapping("/users/internal")
    UserClientResponse createUser(@RequestBody UserClientRequest user);

    @PutMapping("/users/internal")
    UserClientResponse updateUser(@RequestBody UserClientRequest user);
}
