package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.service.PasswordResetService;
import com.pawdcast.pawdcast.application.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/password")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            // Generate reset token
            String resetToken = passwordResetService.generateResetToken(email);
            
            if (resetToken != null) {
                // Send email with reset link
                emailService.sendPasswordResetEmail(email, resetToken);
            }
            
            // Always return success to prevent email enumeration
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "If an account with that email exists, a password reset link has been sent.");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error processing password reset request");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        try {
            // Validate password strength
            if (newPassword == null || newPassword.trim().length() < 6) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Password must be at least 6 characters long");
                return ResponseEntity.badRequest().body(response);
            }

            // Reset password
            boolean success = passwordResetService.resetPassword(token, newPassword);
            
            Map<String, Object> response = new HashMap<>();
            if (success) {
                response.put("success", true);
                response.put("message", "Password has been reset successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid or expired reset token");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error resetting password");
            return ResponseEntity.badRequest().body(response);
        }
    }
}