package com.codepractice.auth_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codepractice.auth_service.model.dto.internal.LoginRequest;
import com.codepractice.auth_service.model.dto.internal.RegisterRequest;
import com.codepractice.auth_service.service.AuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
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
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest registerRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            authService.register(registerRequest);
            redirectAttributes.addFlashAttribute("message", "Registration successful! Please check your email and verify OTP.");
            redirectAttributes.addFlashAttribute("email", registerRequest.getEmail());
            return "redirect:/confirm-registration";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    // Xác nhận đăng ký
    @GetMapping("/confirm-registration")
    public String confirmRegistrationPage(@RequestParam(value = "email", required = false) String email,
            Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "confirm-registration";
    }

    @PostMapping("/confirm-registration")
    public String confirmRegistration(@RequestParam String email,
            @RequestParam String otp,
            RedirectAttributes redirectAttributes) {
        try {
            authService.confirmRegistration(email, otp);
            redirectAttributes.addFlashAttribute("message", "Registration confirmed successfully! Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/confirm-registration";
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
            return "redirect:/reset-password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
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
            return "redirect:/reset-password";
        }

        try {
            authService.resetPassword(email, otp, newPassword);
            redirectAttributes.addFlashAttribute("message", "Password reset successful! Please login with your new password.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("email", email);
            return "redirect:/reset-password";
        }
    }
}