package com.codepractice.user_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codepractice.common_lib.response.ApiResponse;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.model.entity.User;
import com.codepractice.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserInterfaceController {
    private final UserService userService;

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody User user) {
        UserResponse updatedUser = userService.update(user);
        return ResponseEntity.ok(ApiResponse.success(updatedUser, "User updated successfully", HttpStatus.OK.value()));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<ApiResponse<String>> blockUser(@PathVariable long id) {
        userService.block(id);
        return ResponseEntity
                .ok(ApiResponse.success("User blocked successfully", "User blocked successfully",
                        HttpStatus.OK.value()));
    }

    @DeleteMapping("/{id}")
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable long id) {
        UserResponse user = userService.getById(id);
        if (user != null) {
            return ResponseEntity.ok(ApiResponse.success(user, "User found", HttpStatus.OK.value()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found", HttpStatus.NOT_FOUND.value()));
        }
    }
}