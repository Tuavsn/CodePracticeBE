package com.codepractice.auth_service.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.codepractice.auth_service.client.UserClient;
import com.codepractice.auth_service.client.UserPresentation;
import com.codepractice.auth_service.model.dto.request.LoginCredential;
import com.codepractice.auth_service.model.dto.request.RegisterCredential;
import com.codepractice.auth_service.model.dto.response.LoginResponse;
import com.codepractice.auth_service.model.dto.response.RegisterResponse;
import com.codepractice.auth_service.service.AuthService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse LoginWithCredential(LoginCredential credential) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'LoginWithCredential'");
    }

    @Override
    public RegisterResponse Register(RegisterCredential credential) {
        credential.setEmail(credential.getEmail().toLowerCase());

        UserPresentation savedUser = userClient.createUser(UserPresentation.builder()
                .email(null)
                .username(null)
                .password(passwordEncoder.encode(credential.getPassword()))
                .build());

        return RegisterResponse.builder()
            .email(savedUser.getEmail())
            .username(savedUser.getUsername())
            .avatar(savedUser.getAvatar())
            .role(savedUser.getRole())
            .achievement(savedUser.getAchievement())
            .status(savedUser.getStatus())
            // .acc_token()
            // .rf_token()
            .build();
    }

}
