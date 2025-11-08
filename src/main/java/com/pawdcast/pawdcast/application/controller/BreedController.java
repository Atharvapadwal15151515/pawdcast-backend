package com.pawdcast.pawdcast.application.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pawdcast.pawdcast.application.model.Breed;
import com.pawdcast.pawdcast.application.service.BreedService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/breeds")
@CrossOrigin(origins = "*")
public class BreedController {
    
    @Autowired
    private BreedService breedService;
    
    // Get all breeds - PUBLIC
    @GetMapping
    public ResponseEntity<List<Breed>> getAllBreeds() {
        try {
            List<Breed> breeds = breedService.getAllBreeds();
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get breed by ID - PUBLIC
    @GetMapping("/{id}")
    public ResponseEntity<Breed> getBreedById(@PathVariable Long id) {
        try {
            Breed breed = breedService.getBreedById(id);
            if (breed != null) {
                return new ResponseEntity<>(breed, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get breeds by animal type - PUBLIC
    @GetMapping("/type/{animalType}")
    public ResponseEntity<List<Breed>> getBreedsByType(@PathVariable String animalType) {
        try {
            List<Breed> breeds = breedService.getBreedsByAnimalType(animalType);
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Search breeds - PUBLIC
    @GetMapping("/search")
    public ResponseEntity<List<Breed>> searchBreeds(@RequestParam String q) {
        try {
            List<Breed> breeds = breedService.searchBreeds(q);
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Advanced recommendation engine using Map - PUBLIC
    @PostMapping("/recommend")
    public ResponseEntity<List<Breed>> recommendBreeds(@RequestBody Map<String, Object> criteria) {
        try {
            List<Breed> recommendedBreeds = breedService.getRecommendedBreeds(criteria);
            return new ResponseEntity<>(recommendedBreeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Simple recommendation with request parameters - PUBLIC
    @GetMapping("/recommend/simple")
    public ResponseEntity<List<Breed>> simpleRecommendation(
            @RequestParam String animalType,
            @RequestParam String idealHome,
            @RequestParam String activityLevel) {
        try {
            List<Breed> breeds = breedService.getSimpleRecommendation(animalType, idealHome, activityLevel);
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get allergy-friendly breeds - PUBLIC
    @GetMapping("/allergy-friendly/{animalType}")
    public ResponseEntity<List<Breed>> getAllergyFriendlyBreeds(@PathVariable String animalType) {
        try {
            List<Breed> breeds = breedService.getAllergyFriendlyBreeds(animalType);
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create new breed - SECURED (Admin only)
    @PostMapping
    public ResponseEntity<?> createBreed(@RequestBody Breed breed, HttpServletRequest request) {
        try {
            // Check authentication
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
            }
            
            // Optional: Add admin role check here if you have roles
            // if (!userService.isAdmin(userEmail)) {
            //     return new ResponseEntity<>("Admin access required", HttpStatus.FORBIDDEN);
            // }
            
            Breed savedBreed = breedService.saveBreed(breed);
            return new ResponseEntity<>(savedBreed, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update breed - SECURED (Admin only)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBreed(@PathVariable Long id, @RequestBody Breed breed, HttpServletRequest request) {
        try {
            // Check authentication
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
            }
            
            // Optional: Add admin role check here if you have roles
            // if (!userService.isAdmin(userEmail)) {
            //     return new ResponseEntity<>("Admin access required", HttpStatus.FORBIDDEN);
            // }
            
            Breed existingBreed = breedService.getBreedById(id);
            if (existingBreed != null) {
                breed.setId(id);
                Breed updatedBreed = breedService.saveBreed(breed);
                return new ResponseEntity<>(updatedBreed, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Delete breed - SECURED (Admin only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBreed(@PathVariable Long id, HttpServletRequest request) {
        try {
            // Check authentication
            String userEmail = (String) request.getAttribute("userEmail");
            if (userEmail == null) {
                return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
            }
            
            // Optional: Add admin role check here if you have roles
            // if (!userService.isAdmin(userEmail)) {
            //     return new ResponseEntity<>("Admin access required", HttpStatus.FORBIDDEN);
            // }
            
            breedService.deleteBreed(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Health check endpoint - PUBLIC
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Pawdcast Breed API is running");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}