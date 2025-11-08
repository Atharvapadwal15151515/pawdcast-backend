package com.pawdcast.pawdcast.application.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordMigrationService passwordMigrationService;

    public User signup(User user) throws IOException {
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Hash the password before saving
        String hashedPassword = passwordMigrationService.hashPasswordForNewUser(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userOpt.get();

        // Use BCrypt password verification instead of plain text
        if (!passwordMigrationService.verifyPassword(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    // New method for JWT support - find user by email
    public User findByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return userOpt.get();
    }

    // New method for JWT support - find user by ID
    public User findById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        return userOpt.get();
    }

    // Updated method for updating user profile
    public User updateProfile(User user) {
        // Ensure the user exists
        Optional<User> existingUserOpt = userRepository.findById(user.getId());
        if (existingUserOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        // Check if email is being changed and if it's already taken by another user
        User existingUser = existingUserOpt.get();
        if (!existingUser.getEmail().equals(user.getEmail())) {
            Optional<User> emailUserOpt = userRepository.findByEmail(user.getEmail());
            if (emailUserOpt.isPresent() && !emailUserOpt.get().getId().equals(user.getId())) {
                throw new RuntimeException("Email already taken by another user");
            }
        }

        // Update the user fields
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        existingUser.setDateOfBirth(user.getDateOfBirth());
        
        // Only update photo if a new one is provided
        if (user.getPhoto() != null) {
            existingUser.setPhoto(user.getPhoto());
        }
        if (user.getProfilePhoto() != null) {
            existingUser.setProfilePhoto(user.getProfilePhoto());
        }

        return userRepository.save(existingUser);
    }

    // Optional: Method to validate user exists for JWT token
    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    // Optional: Method to get user profile without sensitive data
    public Map<String, Object> getUserProfile(String email) {
        User user = findByEmail(email);
        Map<String, Object> profile = new HashMap<>();
        profile.put("userId", user.getId());
        profile.put("fullName", user.getFullName());
        profile.put("email", user.getEmail());
        profile.put("phone", user.getPhone());
        profile.put("address", user.getAddress());
        profile.put("dateOfBirth", user.getDateOfBirth());
        
        if (user.getProfilePhoto() != null) {
            profile.put("profilePhoto", user.getProfilePhoto());
        }
        if (user.getPhoto() != null) {
            profile.put("photo", user.getPhoto());
        }
        
        return profile;
    }
}