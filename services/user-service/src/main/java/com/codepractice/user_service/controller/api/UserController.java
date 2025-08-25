package com.codepractice.user_service.controller.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.common_lib.response.ApiResponse;
import com.codepractice.user_service.model.dto.request.UpdateProfileRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable long id) {
        userService.hardDelete(id);
        return ResponseEntity
                .ok(ApiResponse.success("User deleted successfully", "User deleted successfully",
                        HttpStatus.OK.value()));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAll();
        return ResponseEntity.ok(ApiResponse.success(users, "Users retrieved successfully", HttpStatus.OK.value()));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        UserResponse user = userService.getProfile();
        return ResponseEntity.ok(ApiResponse.success(user, "Profile retrieved successfully", HttpStatus.OK.value()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody UpdateProfileRequest request) {
        UserResponse updatedProfile = userService.updateProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable long id) {
        UserResponse user = userService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(user, "User found", HttpStatus.OK.value()));
    }
}