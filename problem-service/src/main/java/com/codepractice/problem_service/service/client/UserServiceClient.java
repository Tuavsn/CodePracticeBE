package com.codepractice.problem_service.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.codepractice.problem_service.model.dto.external.UserResponse;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/internal/{id}")
    UserResponse getUserById(@PathVariable Long id);
}
