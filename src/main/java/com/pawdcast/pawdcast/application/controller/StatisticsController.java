package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.repository.UserRepository;
import com.pawdcast.pawdcast.application.repository.PetProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stats")
@CrossOrigin("*")
public class StatisticsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PetProfileRepository petProfileRepository;

    @GetMapping("/counts")
    public ResponseEntity<Map<String, Object>> getCounts() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("totalUsers", userRepository.count());
            response.put("totalPets", petProfileRepository.count());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error fetching statistics");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}