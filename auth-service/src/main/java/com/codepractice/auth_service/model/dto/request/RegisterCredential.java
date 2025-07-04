package com.codepractice.auth_service.model.dto.request;

import lombok.Data;

@Data
public class RegisterCredential {
    private String email;
    private String username;
    private String password;
}