package com.codepractice.auth_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserClient {
    @PostMapping
    UserPresentation createUser(UserPresentation user);

    @GetMapping("/email")
    UserPresentation getUserByEmail(@RequestParam("email") String email);
}