package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.AuthService;
import com.pawdcast.pawdcast.application.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    // Signup - No changes needed as it doesn't use session
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(required = false) MultipartFile profilePhoto
    ) {
        try {
            byte[] photoBytes = null;
            byte[] profilePhotoBytes = null;

            if (photo != null && !photo.isEmpty()) {
                photoBytes = photo.getBytes();
            }
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                profilePhotoBytes = profilePhoto.getBytes();
            }

            User user = new User(fullName, email, password, phone, address, photoBytes, dateOfBirth, profilePhotoBytes);

            User savedUser = authService.signup(user);
            return ResponseEntity.ok(savedUser);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing uploaded files: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login - Updated for JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        try {
            User user = authService.login(email, password);
            
            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());

            // Enhanced response with JWT token and all user details needed for ecommerce
            Map<String, Object> response = new HashMap<>();
            response.put("token", token); // JWT token
            response.put("userId", user.getId());
            response.put("fullName", user.getFullName());
            response.put("email", user.getEmail());
            response.put("phone", user.getPhone());
            response.put("address", user.getAddress()); // Crucial for shipping
            response.put("dateOfBirth", user.getDateOfBirth());
            response.put("message", "Login successful");
            
            // Add profile photo if exists
            if (user.getProfilePhoto() != null) {
                response.put("profilePhoto", user.getProfilePhoto());
            }
            if (user.getPhoto() != null) {
                response.put("photo", user.getPhoto());
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // Logout - Updated for JWT (client-side token removal)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // With JWT, logout is handled client-side by removing the token
        // This endpoint can be used for server-side cleanup if needed
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful - please remove token client-side");
        return ResponseEntity.ok(response);
    }

    // Get current logged-in user - Updated for JWT
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        // Get email from JWT filter (replaces session.getAttribute("user"))
        String email = (String) request.getAttribute("userEmail");
        if (email == null) {
            return ResponseEntity.status(401).body("No user logged in");
        }
        
        // Fetch user from database using email
        User user = authService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }
        
        // Return user details without sensitive data
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone", user.getPhone());
        response.put("address", user.getAddress());
        response.put("dateOfBirth", user.getDateOfBirth());
        
        // Add profile photo if exists
        if (user.getProfilePhoto() != null) {
            response.put("profilePhoto", user.getProfilePhoto());
        }
        if (user.getPhoto() != null) {
            response.put("photo", user.getPhoto());
        }
        
        return ResponseEntity.ok(response);
    }

    // Check authentication status - Updated for JWT
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        String email = (String) request.getAttribute("userEmail");
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", email != null);
        
        if (email != null) {
            User user = authService.findByEmail(email);
            if (user != null) {
                response.put("userId", user.getId());
                response.put("fullName", user.getFullName());
                response.put("email", user.getEmail());
            }
        }
        
        return ResponseEntity.ok(response);
    }

    // Update user profile - Updated for JWT
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile profilePhoto,
            HttpServletRequest request
    ) {
        try {
            // Get email from JWT filter
            String email = (String) request.getAttribute("userEmail");
            if (email == null) {
                return ResponseEntity.status(401).body("User not logged in");
            }

            // Fetch current user from database
            User currentUser = authService.findByEmail(email);
            if (currentUser == null) {
                return ResponseEntity.status(401).body("User not found");
            }

            // Update user details
            currentUser.setFullName(fullName);
            currentUser.setPhone(phone);
            currentUser.setAddress(address); // Important for shipping updates

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                byte[] profilePhotoBytes = profilePhoto.getBytes();
                currentUser.setProfilePhoto(profilePhotoBytes);
            }

            // Save updated user
            User updatedUser = authService.updateProfile(currentUser);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", updatedUser.getId());
            response.put("fullName", updatedUser.getFullName());
            response.put("email", updatedUser.getEmail());
            response.put("phone", updatedUser.getPhone());
            response.put("address", updatedUser.getAddress());
            response.put("message", "Profile updated successfully");

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error processing uploaded file: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}