package com.codepractice.auth_service.model.dto.internal;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
