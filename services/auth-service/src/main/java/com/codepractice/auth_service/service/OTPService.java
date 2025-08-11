package com.codepractice.auth_service.service;

public interface OTPService {
    public String generateOTP(String email);
    
    public boolean validateOTP(String email, String otp);

    public void invalidateOTP(String email);
}
