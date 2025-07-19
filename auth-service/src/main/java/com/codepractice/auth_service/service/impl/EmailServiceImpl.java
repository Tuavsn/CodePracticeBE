package com.codepractice.auth_service.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.codepractice.auth_service.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    public void sendPasswordRegisterEmail(String email, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Welcome to CodeJudge - Confirm your Registration");

            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("email", email);

            String htmlContent = templateEngine.process("email/registration-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Registration email sent successfully to: {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send registration email to: {}", email, e);
            throw new RuntimeException("Failed to send registration email", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            helper.setTo(email);
            helper.setSubject("CodeJudge - Password Reset Request");
            
            Context context = new Context();
            context.setVariable("otp", otp);
            context.setVariable("email", email);
            
            String htmlContent = templateEngine.process("email/password-reset", context);
            helper.setText(htmlContent, true);
            
            mailSender.send(mimeMessage);
            log.info("Password reset email sent successfully to: {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to: {}", email, e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

}
