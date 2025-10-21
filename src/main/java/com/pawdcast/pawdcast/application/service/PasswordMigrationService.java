package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PasswordMigrationService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void migrateExistingPasswords() {
        List<User> allUsers = userRepository.findAll();
        int migrated = 0;
        int skipped = 0;
        
        System.out.println("=== Starting Password Migration ===");
        
        for (User user : allUsers) {
            String currentPassword = user.getPassword();
            
            // Check if password is already hashed
            if (isPasswordHashed(currentPassword)) {
                System.out.println("✓ Already hashed: " + user.getEmail());
                skipped++;
                continue;
            }
            
            // Hash the plain text password
            try {
                String hashedPassword = BCrypt.hashpw(currentPassword, BCrypt.gensalt(12));
                user.setPassword(hashedPassword);
                userRepository.save(user);
                migrated++;
                System.out.println("✓ Migrated: " + user.getEmail());
            } catch (Exception e) {
                System.err.println("✗ Failed: " + user.getEmail() + " - " + e.getMessage());
            }
        }
        
        System.out.println("=== Migration Complete ===");
        System.out.println("Total: " + allUsers.size() + ", Migrated: " + migrated + ", Skipped: " + skipped);
    }

    public String hashPasswordForNewUser(String plainPassword) {
        if (isPasswordHashed(plainPassword)) {
            return plainPassword;
        }
        
        try {
            return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash password: " + e.getMessage());
        }
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        
        try {
            // If the stored password is not hashed (plain text), compare directly
            if (!isPasswordHashed(hashedPassword)) {
                boolean result = plainPassword.equals(hashedPassword);
                System.out.println("Plain text comparison: " + result);
                return result;
            }
            
            // If it's hashed, use BCrypt verification
            boolean result = BCrypt.checkpw(plainPassword, hashedPassword);
            System.out.println("BCrypt verification: " + result);
            return result;
        } catch (Exception e) {
            System.err.println("Password verification error: " + e.getMessage());
            return false;
        }
    }

    // CHANGED FROM PRIVATE TO PUBLIC
    public boolean isPasswordHashed(String password) {
        if (password == null) return false;
        
        // BCrypt hashed passwords start with $2a$, $2b$, or $2y$ and are 60 chars long
        return password.length() == 60 && 
               (password.startsWith("$2a$") || 
                password.startsWith("$2b$") || 
                password.startsWith("$2y$"));
    }

    // Debug method to check password status
    public void debugUserPassword(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String password = user.getPassword();
            System.out.println("=== DEBUG: " + email + " ===");
            System.out.println("Password: " + password);
            System.out.println("Is hashed: " + isPasswordHashed(password));
            System.out.println("Length: " + (password != null ? password.length() : "null"));
        } else {
            System.out.println("User not found: " + email);
        }
    }
}