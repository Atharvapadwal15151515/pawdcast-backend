package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final int BCRYPT_WORK_FACTOR = 12;

    // Signup user with password hashing
    public User signup(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return null; // Email already exists
        }
        
        // Hash the password before saving
        String hashedPassword = hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        
        return userRepository.save(user);
    }

    // Login user with password verification
    public User login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && verifyPassword(password, user.get().getPassword())) {
            return user.get();
        }
        return null;
    }

    // Get user by ID
    public User getUserById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // Hash password using BCrypt
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_WORK_FACTOR));
    }

    // Verify password against hashed password
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}