package com.codepractice.auth_service.service.impl;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.codepractice.auth_service.service.OTPService;

import lombok.AllArgsConstructor;
import lombok.Data;

@Service
public class OTPServiceImpl implements OTPService {
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @Override
    public String generateOTP(String email) {
        String otp = String.format("%06d", random.nextInt(1000000));

        otpStore.put(email, new OtpData(otp, LocalDateTime.now().plusMinutes(5)));
        
        return otp;
    }

    @Override
    public boolean validateOTP(String email, String otp) {
        OtpData otpData = otpStore.get(email);

        if (otpData == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            otpStore.remove(email);
            return false;
        }

        return otpData.getOtp().equals(otp);
    }

    @Override
    public void invalidateOTP(String email) {
        otpStore.remove(email);
    }

    @Data
    @AllArgsConstructor
    private static class OtpData {
        private String otp;
        private LocalDateTime expiryTime;
    }
}
