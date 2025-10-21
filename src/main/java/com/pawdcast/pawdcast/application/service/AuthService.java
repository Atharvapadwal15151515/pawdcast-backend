package com.pawdcast.pawdcast.application.service;

import java.io.IOException;
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

    // New method for updating user profile
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
}