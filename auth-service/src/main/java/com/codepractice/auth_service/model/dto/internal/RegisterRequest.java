package com.codepractice.auth_service.model.dto.internal;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
