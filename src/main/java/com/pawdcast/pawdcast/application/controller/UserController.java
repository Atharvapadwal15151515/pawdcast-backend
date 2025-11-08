package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.UserService;
import com.pawdcast.pawdcast.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    private User getCurrentUser(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            throw new RuntimeException("User not authenticated");
        }
        return authService.findByEmail(userEmail);
    }

    // Signup endpoint - PUBLIC (no changes needed)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = false) MultipartFile profilePhoto
    ) throws IOException {
        byte[] photoBytes = null;
        byte[] profilePhotoBytes = null;

        if (photo != null && !photo.isEmpty()) {
            photoBytes = photo.getBytes();
        }

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            profilePhotoBytes = profilePhoto.getBytes();
        }

        LocalDate dob = null;
        if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dob = LocalDate.parse(dateOfBirth, formatter);
        }

        User user = new User(fullName, email, password, phone, address, photoBytes, dob, profilePhotoBytes);
        User createdUser = userService.signup(user);

        if (createdUser == null) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }
        return ResponseEntity.ok(createdUser);
    }

    // Login endpoint - PUBLIC (no changes needed)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.login(email, password);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials.");
        }
        return ResponseEntity.ok(user);
    }

    // Get current user's profile - SECURED
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserProfile(HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            // Return user data (consider excluding sensitive fields like password)
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // Get user by ID - SECURED with ownership validation
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id, HttpServletRequest request) {
        try {
            User currentUser = getCurrentUser(request);
            
            // Users can only access their own data (unless admin)
            if (!currentUser.getId().equals(id)) {
                return ResponseEntity.status(403).body("Access denied");
            }
            
            User user = userService.getUserById(id);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Consider returning a DTO without sensitive data
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}