package com.codepractice.auth_service.service;

import com.codepractice.auth_service.model.dto.internal.RegisterRequest;

public interface AuthService {
    public void register(RegisterRequest request);

    public void sendPasswordResetEmail(String email);

    public void sendPasswordRegisterEmail(String email);

    public void resetPassword(String email, String otp, String newPassword);

    public void confirmRegistration(String email, String otp);
}
