package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserRegistrationController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            // Validate required fields
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email is required"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password is required"));
            }

            User user = userRegistrationService.registerUser(
                request.getFullName(),
                request.getEmail().trim(),
                request.getPassword(),
                request.getPhone(),
                request.getAddress()
            );

            Map<String, Object> response = createSuccessResponse("User registered successfully");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register/full")
    public ResponseEntity<?> registerUserFull(@RequestBody UserRegistrationFullRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email is required"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password is required"));
            }

            // Convert base64 photo strings to byte arrays if provided
            byte[] photo = null;
            byte[] profilePhoto = null;
            
            try {
                if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
                    photo = java.util.Base64.getDecoder().decode(request.getPhoto());
                }
                if (request.getProfilePhoto() != null && !request.getProfilePhoto().isEmpty()) {
                    profilePhoto = java.util.Base64.getDecoder().decode(request.getProfilePhoto());
                }
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(createErrorResponse("Invalid photo format: " + e.getMessage()));
            }

            User user = userRegistrationService.registerUser(
                request.getFullName(),
                request.getEmail().trim(),
                request.getPassword(),
                request.getPhone(),
                request.getAddress(),
                photo,
                request.getDateOfBirth(),
                profilePhoto
            );

            Map<String, Object> response = createSuccessResponse("User registered successfully");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        try {
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email is required"));
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Password is required"));
            }

            // Use the enhanced login method
            Optional<User> userOptional = userRegistrationService.loginUser(
                request.getEmail().trim(), 
                request.getPassword()
            );

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                // Don't send password in response
                user.setPassword(null);
                
                Map<String, Object> response = createSuccessResponse("Login successful");
                response.put("user", user);
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401)
                    .body(createErrorResponse("Invalid email or password"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(createErrorResponse("Login failed: " + e.getMessage()));
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Integer userId, @RequestBody PasswordUpdateRequest request) {
        try {
            if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("New password is required"));
            }

            userRegistrationService.updatePassword(userId, request.getNewPassword());

            return ResponseEntity.ok(createSuccessResponse("Password updated successfully"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("Password update failed: " + e.getMessage()));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Integer userId) {
        try {
            Optional<User> userOptional = userRegistrationService.getUserById(userId);
            
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setPassword(null); // Don't expose password
                
                Map<String, Object> response = createSuccessResponse("User profile retrieved");
                response.put("user", user);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(404).body(createErrorResponse("User not found"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(createErrorResponse("Failed to fetch user profile: " + e.getMessage()));
        }
    }

    // Helper methods for consistent responses
    private Map<String, Object> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        return response;
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }

    // Request DTO classes
    public static class UserRegistrationRequest {
        private String fullName;
        private String email;
        private String password;
        private String phone;
        private String address;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class UserRegistrationFullRequest {
        private String fullName;
        private String email;
        private String password;
        private String phone;
        private String address;
        private String photo;
        private LocalDate dateOfBirth;
        private String profilePhoto;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getPhoto() { return photo; }
        public void setPhoto(String photo) { this.photo = photo; }
        public LocalDate getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
        public String getProfilePhoto() { return profilePhoto; }
        public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class PasswordUpdateRequest {
        private String newPassword;

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}