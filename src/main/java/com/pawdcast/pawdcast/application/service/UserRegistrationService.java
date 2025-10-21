package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordMigrationService passwordMigrationService;

    @Transactional
    public User registerUser(String fullName, String email, String password, String phone, String address) {
        return registerUser(fullName, email, password, phone, address, null, null, null);
    }

    @Transactional
    public User registerUser(String fullName, String email, String password, String phone, String address,
                           byte[] photo, java.time.LocalDate dateOfBirth, byte[] profilePhoto) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists with email: " + email);
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        user.setPhoto(photo);
        user.setDateOfBirth(dateOfBirth);
        user.setProfilePhoto(profilePhoto);
        
        // Hash the password
        String hashedPassword = passwordMigrationService.hashPasswordForNewUser(password);
        user.setPassword(hashedPassword);

        User savedUser = userRepository.save(user);
        System.out.println("✓ New user registered: " + email);
        return savedUser;
    }

    @Transactional
    public void updatePassword(Integer userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String hashedPassword = passwordMigrationService.hashPasswordForNewUser(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        System.out.println("✓ Password updated for user ID: " + userId);
    }

    /**
     * Enhanced login verification with detailed logging
     */
    public boolean verifyLogin(String email, String password) {
        System.out.println("=== Login Attempt ===");
        System.out.println("Email: " + email);
        
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            System.out.println("✗ User not found: " + email);
            return false;
        }
        
        User user = userOptional.get();
        String storedPassword = user.getPassword();
        
        System.out.println("Stored password type: " + (passwordMigrationService.isPasswordHashed(storedPassword) ? "Hashed" : "Plain text"));
        System.out.println("Stored password preview: " + (storedPassword != null ? storedPassword.substring(0, Math.min(10, storedPassword.length())) + "..." : "null"));
        
        boolean isValid = passwordMigrationService.verifyPassword(password, storedPassword);
        
        System.out.println("Password verification: " + (isValid ? "✓ SUCCESS" : "✗ FAILED"));
        System.out.println("=====================");
        return isValid;
    }

    /**
     * Enhanced login that returns the user if valid
     */
    public Optional<User> loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isPresent() && 
            passwordMigrationService.verifyPassword(password, userOptional.get().getPassword())) {
            return userOptional;
        }
        
        return Optional.empty();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }
}