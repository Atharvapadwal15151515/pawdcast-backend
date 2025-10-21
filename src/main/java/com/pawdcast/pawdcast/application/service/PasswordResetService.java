package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.PasswordResetToken;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.repository.PasswordResetTokenRepository;
import com.pawdcast.pawdcast.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordMigrationService passwordMigrationService;

    private static final int TOKEN_EXPIRY_HOURS = 24;

    public String generateResetToken(String email) {
        // Check if user exists
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            return null; // Don't reveal if user exists
        }

        // Delete any existing tokens for this email
        tokenRepository.deleteAllByEmail(email);

        // Generate new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS);

        PasswordResetToken resetToken = new PasswordResetToken(token, email, expiryDate);
        tokenRepository.save(resetToken);

        return token;
    }

    public boolean isValidToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        return tokenOpt.isPresent() && tokenOpt.get().isValid();
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        
        if (tokenOpt.isEmpty() || !tokenOpt.get().isValid()) {
            return false;
        }

        PasswordResetToken resetToken = tokenOpt.get();
        String email = resetToken.getEmail();

        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        
        // Hash the new password using existing service
        String hashedPassword = passwordMigrationService.hashPasswordForNewUser(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return true;
    }

    public void cleanupExpiredTokens() {
        tokenRepository.deleteAllExpiredTokens(LocalDateTime.now());
    }

    public String getEmailFromToken(String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        return tokenOpt.map(PasswordResetToken::getEmail).orElse(null);
    }
}