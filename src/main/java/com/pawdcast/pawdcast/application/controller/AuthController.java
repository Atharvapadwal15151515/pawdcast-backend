package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.AuthService;
import jakarta.servlet.http.HttpSession;
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

    // Signup
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

    // Login - Enhanced for ecommerce
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session
    ) {
        try {
            User user = authService.login(email, password);
            session.setAttribute("user", user);

            // Enhanced response with all user details needed for ecommerce
            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getId());
            response.put("fullName", user.getFullName());
            response.put("email", user.getEmail());
            response.put("phone", user.getPhone());
            response.put("address", user.getAddress()); // Crucial for shipping
            response.put("dateOfBirth", user.getDateOfBirth());
            
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

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully!");
    }

    // Get current logged-in user - Enhanced for ecommerce
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body("No user logged in");
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

    // Check authentication status - Useful for frontend
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", user != null);
        
        if (user != null) {
            response.put("userId", user.getId());
            response.put("fullName", user.getFullName());
            response.put("email", user.getEmail());
        }
        
        return ResponseEntity.ok(response);
    }

    // Update user profile - Useful for ecommerce (address, phone updates)
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @RequestParam String fullName,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile profilePhoto,
            HttpSession session
    ) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return ResponseEntity.status(401).body("User not logged in");
            }

            // Update user details
            currentUser.setFullName(fullName);
            currentUser.setPhone(phone);
            currentUser.setAddress(address); // Important for shipping updates

            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                byte[] profilePhotoBytes = profilePhoto.getBytes();
                currentUser.setProfilePhoto(profilePhotoBytes);
            }

            // Save updated user (you might need to add this method to AuthService)
            User updatedUser = authService.updateProfile(currentUser);
            session.setAttribute("user", updatedUser); // Update session

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