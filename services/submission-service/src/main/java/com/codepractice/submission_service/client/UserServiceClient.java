package com.codepractice.submission_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codepractice.submission_service.model.dto.internal.UpdateUserStatsRequest;
import com.codepractice.submission_service.model.dto.internal.UserClientResponse;

@FeignClient("user-service")
public interface UserServiceClient {
    @GetMapping("/users/internal/{id}")
    UserClientResponse getUserById(@PathVariable Long id);

    @PostMapping("/users/internal/update-stats")
    void updateUserStats(@RequestBody UpdateUserStatsRequest request);
}
