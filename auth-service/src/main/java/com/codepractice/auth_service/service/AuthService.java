package com.codepractice.auth_service.service;

import com.codepractice.auth_service.model.dto.request.LoginCredential;
import com.codepractice.auth_service.model.dto.request.RegisterCredential;
import com.codepractice.auth_service.model.dto.response.LoginResponse;
import com.codepractice.auth_service.model.dto.response.RegisterResponse;

public interface AuthService {
    public LoginResponse LoginWithCredential(LoginCredential credential);

    public RegisterResponse Register(RegisterCredential credential);
}