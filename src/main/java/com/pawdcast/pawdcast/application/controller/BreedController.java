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

@RestController
@RequestMapping("/api/breeds")
@CrossOrigin(origins = "*")
public class BreedController {
    
    @Autowired
    private BreedService breedService;
    
    // Get all breeds
    @GetMapping
    public ResponseEntity<List<Breed>> getAllBreeds() {
        try {
            List<Breed> breeds = breedService.getAllBreeds();
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Get breed by ID
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
    
    // Get breeds by animal type
    @GetMapping("/type/{animalType}")
    public ResponseEntity<List<Breed>> getBreedsByType(@PathVariable String animalType) {
        try {
            List<Breed> breeds = breedService.getBreedsByAnimalType(animalType);
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Search breeds
    @GetMapping("/search")
    public ResponseEntity<List<Breed>> searchBreeds(@RequestParam String q) {
        try {
            List<Breed> breeds = breedService.searchBreeds(q);
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Advanced recommendation engine using Map
    @PostMapping("/recommend")
    public ResponseEntity<List<Breed>> recommendBreeds(@RequestBody Map<String, Object> criteria) {
        try {
            List<Breed> recommendedBreeds = breedService.getRecommendedBreeds(criteria);
            return new ResponseEntity<>(recommendedBreeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Simple recommendation with request parameters
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
    
    // Get allergy-friendly breeds
    @GetMapping("/allergy-friendly/{animalType}")
    public ResponseEntity<List<Breed>> getAllergyFriendlyBreeds(@PathVariable String animalType) {
        try {
            List<Breed> breeds = breedService.getAllergyFriendlyBreeds(animalType);
            return new ResponseEntity<>(breeds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Create new breed
    @PostMapping
    public ResponseEntity<Breed> createBreed(@RequestBody Breed breed) {
        try {
            Breed savedBreed = breedService.saveBreed(breed);
            return new ResponseEntity<>(savedBreed, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Update breed
    @PutMapping("/{id}")
    public ResponseEntity<Breed> updateBreed(@PathVariable Long id, @RequestBody Breed breed) {
        try {
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
    
    // Delete breed
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBreed(@PathVariable Long id) {
        try {
            breedService.deleteBreed(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("message", "Pawdcast Breed API is running");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}