package com.codepractice.auth_service.service;

public interface EmailService {
    public void sendPasswordRegisterEmail(String email, String otp);

    public void sendPasswordResetEmail(String email, String otp);
}
