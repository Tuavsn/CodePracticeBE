package com.codepractice.auth_service.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codepractice.auth_service.enums.AccountRole;
import com.codepractice.auth_service.enums.AccountStatus;
import com.codepractice.auth_service.enums.AuthStrategy;
import com.codepractice.auth_service.model.dto.external.UserRequest;
import com.codepractice.auth_service.model.dto.internal.RegisterRequest;
import com.codepractice.auth_service.model.entity.User;
import com.codepractice.auth_service.service.AuthService;
import com.codepractice.auth_service.service.EmailService;
import com.codepractice.auth_service.service.OTPService;
import com.codepractice.auth_service.service.UserService;
import com.codepractice.auth_service.service.client.UserServiceClient;
import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {
    private final UserServiceClient userClientService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OTPService otpService;
    private final EmailService emailService;

    @Override
    public void register(RegisterRequest request) {
        validateRequest(request);
        
        User newUser = buildNewUser(request);
        User savedUser = userService.create(newUser);
        UserRequest externalUser = buildExternalUser(savedUser);

        userClientService.createUser(externalUser);

        sendActivationOtp(request.getEmail());
        log.info("Registration initiated for email={}", savedUser.getEmail());
    }
    
    @Override
    public void sendPasswordResetEmail(String email) {
        User user = fetchActiveUser(email);
        sendPasswordResetOtp(user.getEmail());
        log.info("Password reset OTP sent to email={}", email);
    }
    
    @Override
    public void sendPasswordRegisterEmail(String email) {
        User user = fetchInactiveUser(email);
        sendActivationOtp(user.getEmail());

    }
    
    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        validateReset(email, otp, newPassword);

        User user = fetchActiveUser(email);
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.update(user);

        otpService.invalidateOTP(email);

        log.info("Password reset successful for email={}", email);
    }
    
    @Override
    public void confirmRegistration(String email, String otp) {
        if (!otpService.validateOTP(email, otp)) {
            throw new AppException(ErrorCode.INVALID_OTP);
        }

        User user = fetchInactiveUser(email);
        user.setStatus(AccountStatus.ACTIVE);
        userService.update(user);

        otpService.invalidateOTP(email);

        log.info("Account activated for email={}", email);
    }

    /* Helpers */

    private void validateRequest(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD_CONFIRM, "Password and confirmPassword do not match");
        }
        if (userService.getByEmail(request.getEmail()) != null) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_REGISTERED, "Email already registered");
        }
        if (request.getPassword().length() < 8) {
            throw new AppException(ErrorCode.WEAK_PASSWORD, "Password must be at least 8 characters");
        }
    }

    private void validateReset(String email, String otp, String newPassword) {
        if (email == null || otp == null || newPassword == null || email.isBlank() || otp.isBlank() || newPassword.isBlank()) {
            throw new AppException(ErrorCode.MISSING_REQUIRED_FIELDS, "Email, OTP and newPassword are required");
        }
        if (!otpService.validateOTP(email, otp)) {
            throw new AppException(ErrorCode.INVALID_OTP, "Invalid or expired OTP");
        }
        if (newPassword.length() < 8) {
            throw new AppException(ErrorCode.WEAK_PASSWORD, "Password must be at least 8 characters");
        }
    }

    private User buildNewUser(RegisterRequest request) {
        return User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(AccountRole.USER)
                .authStrategy(AuthStrategy.LOCAL)
                .status(AccountStatus.INACTIVE)
                .build();
    }

    private UserRequest buildExternalUser(User user) {
        return UserRequest.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    private User fetchActiveUser(String email) {
        User user = userService.getByEmail(email);

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new AppException(ErrorCode.INACTIVE_ACCOUNT);
        }

        return user;
    }
    
    private User fetchInactiveUser(String email) {
        User user = userService.getByEmail(email);

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (user.getStatus() != AccountStatus.INACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_ALREADY_ACTIVE);
        }

        return user;
    }

    private void sendActivationOtp(String email) {
        String otp = otpService.generateOTP(email);
        emailService.sendPasswordRegisterEmail(email, otp);
    }

    private void sendPasswordResetOtp(String email) {
        String otp = otpService.generateOTP(email);
        emailService.sendPasswordResetEmail(email, otp);
    }
}
