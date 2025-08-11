package com.codepractice.auth_service.controller.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codepractice.auth_service.model.dto.request.LoginRequest;
import com.codepractice.auth_service.model.dto.request.RegisterRequest;
import com.codepractice.auth_service.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    @Value("${auth.server.gatewayClientUrl}")
    private String gatewayClientUrl;

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        log.info("Login page accessed with error: {}, logout: {}", error, logout);
        
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        log.info("Register page accessed");
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest registerRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        log.info("Register POST request received for email: {}", registerRequest.getEmail());
        
        if (registerRequest.getUsername() == null || registerRequest.getUsername().trim().isEmpty()) {
            bindingResult.rejectValue("username", "error.username", "Username is required");
        }
        
        if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
            bindingResult.rejectValue("email", "error.email", "Email is required");
        }
        
        if (registerRequest.getPassword() == null || registerRequest.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "error.password", "Password is required");
        }
        
        if (registerRequest.getConfirmPassword() == null || 
            !registerRequest.getConfirmPassword().equals(registerRequest.getPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword", 
                "Passwords do not match");
        }

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in registration: {}", bindingResult.getAllErrors());
            model.addAttribute("registerRequest", registerRequest);
            return "register";
        }

        try {
            authService.register(registerRequest);
            log.info("Registration successful for email: {}", registerRequest.getEmail());
            
            redirectAttributes.addFlashAttribute("message", 
                "Registration successful! Please check your email and verify OTP.");
            redirectAttributes.addFlashAttribute("email", registerRequest.getEmail());
            return "redirect:" + gatewayClientUrl + "/confirm-registration";
            
        } catch (Exception e) {
            log.error("Registration failed for email: {}, error: {}", 
                registerRequest.getEmail(), e.getMessage(), e);
            
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", registerRequest);
            return "register"; // Trả về trang register với lỗi thay vì redirect
        }
    }

    // Xác nhận đăng ký
    @GetMapping("/confirm-registration")
    public String confirmRegistrationPage(@RequestParam(value = "email", required = false) String email,
            Model model) {
        log.info("Confirm registration page accessed for email: {}", email);
        
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "confirm-registration";
    }

    @PostMapping("/confirm-registration")
    public String confirmRegistration(@RequestParam String email,
            @RequestParam String otp,
            RedirectAttributes redirectAttributes) {
        log.info("Confirm registration POST request for email: {}", email);
        
        try {
            authService.confirmRegistration(email, otp);
            log.info("Registration confirmed successfully for email: {}", email);
            
            redirectAttributes.addFlashAttribute("message", 
                "Registration confirmed successfully! Please login.");
            return "redirect:" + gatewayClientUrl + "/login";
            
        } catch (Exception e) {
            log.error("Registration confirmation failed for email: {}, error: {}", 
                email, e.getMessage(), e);
            
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:" + gatewayClientUrl + "/confirm-registration";
        }
    }

    // Quên mật khẩu
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
            RedirectAttributes redirectAttributes) {
        try {
            authService.sendPasswordResetEmail(email);
            redirectAttributes.addFlashAttribute("message", "Password reset email sent! Please check your email.");
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:" + gatewayClientUrl + "/reset-password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + gatewayClientUrl + "/forgot-password";
        }
    }

    // Reset mật khẩu
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam(value = "email", required = false) String email,
            Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        
        // Kiểm tra mật khẩu khớp
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match!");
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:" + gatewayClientUrl + "/reset-password";
        }

        try {
            authService.resetPassword(email, otp, newPassword);
            redirectAttributes.addFlashAttribute("message", "Password reset successful! Please login with your new password.");
            return "redirect:" + gatewayClientUrl + "/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:" + gatewayClientUrl + "/reset-password";
        }
    }
}