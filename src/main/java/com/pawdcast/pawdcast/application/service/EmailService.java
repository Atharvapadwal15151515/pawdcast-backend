package com.pawdcast.pawdcast.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Pawdcast - Password Reset Request");
        message.setText(
            "Hello,\n\n" +
            "You requested to reset your password for your Pawdcast account.\n\n" +
            "Please click the link below to reset your password:\n" +
            resetLink + "\n\n" +
            "This link will expire in 24 hours.\n\n" +
            "If you didn't request this, please ignore this email.\n\n" +
            "Best regards,\n" +
            "Pawdcast Team"
        );
        
        mailSender.send(message);
    }
}