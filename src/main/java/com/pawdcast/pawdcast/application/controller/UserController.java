package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Signup endpoint - NO CHANGES NEEDED
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

    // Login endpoint - NO CHANGES NEEDED
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        User user = userService.login(email, password);
        if (user == null) {
            return ResponseEntity.status(401).body("Invalid credentials.");
        }
        return ResponseEntity.ok(user);
    }

    // Get user by ID - NO CHANGES NEEDED
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}