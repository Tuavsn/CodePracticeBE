package com.codepractice.forum_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codepractice.forum_service.model.dto.internal.UserClientResponse;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/users/internal/{id}")
    UserClientResponse getUserById(@PathVariable Long id);
}
