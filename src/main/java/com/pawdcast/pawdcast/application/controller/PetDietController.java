package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.PetDietGuidelines;
import com.pawdcast.pawdcast.application.model.PetDietTips;
import com.pawdcast.pawdcast.application.service.PetDietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pet-diet")
@CrossOrigin(origins = "*")
public class PetDietController {
    
    @Autowired
    private PetDietService petDietService;
    
    // Get all diet tips
    @GetMapping("/tips")
    public ResponseEntity<?> getAllDietTips() {
        try {
            PetDietTips tips = petDietService.getPetDietTips();
            return ResponseEntity.ok(tips);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading diet tips: " + e.getMessage());
        }
    }
    
    // Get filtered diet tips by category
    @GetMapping("/tips/filter")
    public ResponseEntity<?> getFilteredTips(@RequestParam(required = false) String category) {
        try {
            PetDietTips tips = petDietService.getFilteredTips(category);
            return ResponseEntity.ok(tips);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading filtered tips: " + e.getMessage());
        }
    }
    
    // Get all diet guidelines
    @GetMapping("/guidelines")
    public ResponseEntity<?> getAllDietGuidelines() {
        try {
            PetDietGuidelines guidelines = petDietService.getPetDietGuidelines();
            return ResponseEntity.ok(guidelines);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading diet guidelines: " + e.getMessage());
        }
    }
    
    // Get filtered guidelines by category
    @GetMapping("/guidelines/filter")
    public ResponseEntity<?> getFilteredGuidelines(@RequestParam(required = false) String category) {
        try {
            PetDietGuidelines guidelines = petDietService.getFilteredGuidelines(category);
            return ResponseEntity.ok(guidelines);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading filtered guidelines: " + e.getMessage());
        }
    }
    
    // Get combined tips and guidelines for specific pet type
    @GetMapping("/pet-type/{petType}")
    public ResponseEntity<?> getTipsByPetType(@PathVariable String petType) {
        try {
            Object tips = petDietService.getTipsByPetType(petType);
            return ResponseEntity.ok(tips);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading tips for pet type: " + e.getMessage());
        }
    }
    
    // Get emergency signs only
    @GetMapping("/emergency-signs")
    public ResponseEntity<?> getEmergencySigns() {
        try {
            PetDietTips tips = petDietService.getPetDietTips();
            Map<String, Object> response = new HashMap<>();
            response.put("emergencySigns", tips.getEmergencySigns());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading emergency signs: " + e.getMessage());
        }
    }
    
    // Get foods to avoid
    @GetMapping("/foods-to-avoid")
    public ResponseEntity<?> getFoodsToAvoid() {
        try {
            PetDietGuidelines guidelines = petDietService.getPetDietGuidelines();
            Map<String, Object> response = new HashMap<>();
            response.put("foodsToAvoid", guidelines.getFoodsToAvoid());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading foods to avoid: " + e.getMessage());
        }
    }
    
    // Get recommended foods
    @GetMapping("/recommended-foods")
    public ResponseEntity<?> getRecommendedFoods() {
        try {
            PetDietGuidelines guidelines = petDietService.getPetDietGuidelines();
            Map<String, Object> response = new HashMap<>();
            response.put("recommendedFoods", guidelines.getRecommendedFoods());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading recommended foods: " + e.getMessage());
        }
    }
    
    // Get feeding guidelines
    @GetMapping("/feeding-guidelines")
    public ResponseEntity<?> getFeedingGuidelines() {
        try {
            PetDietGuidelines guidelines = petDietService.getPetDietGuidelines();
            Map<String, Object> response = new HashMap<>();
            response.put("feedingGuidelines", guidelines.getFeedingGuidelines());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading feeding guidelines: " + e.getMessage());
        }
    }
    
    // Get all diet information in one endpoint
    @GetMapping("/all")
    public ResponseEntity<?> getAllDietInformation() {
        try {
            PetDietTips tips = petDietService.getPetDietTips();
            PetDietGuidelines guidelines = petDietService.getPetDietGuidelines();
            
            Map<String, Object> response = new HashMap<>();
            response.put("tips", tips);
            response.put("guidelines", guidelines);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error loading all diet information: " + e.getMessage());
        }
    }
    
    // Search tips and guidelines
    @GetMapping("/search")
    public ResponseEntity<?> searchDietInformation(@RequestParam String query) {
        try {
            PetDietTips tips = petDietService.getPetDietTips();
            PetDietGuidelines guidelines = petDietService.getPetDietGuidelines();
            
            Map<String, Object> searchResults = new HashMap<>();
            
            // Search in tips
            if (tips.getGeneralTips() != null) {
                tips.getGeneralTips().forEach(category -> {
                    if (category.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                        category.getTips().stream().anyMatch(tip -> tip.toLowerCase().contains(query.toLowerCase()))) {
                        searchResults.put("generalTips", category);
                    }
                });
            }
            
            // Search in emergency signs
            if (tips.getEmergencySigns() != null) {
                var matchingSigns = tips.getEmergencySigns().stream()
                        .filter(sign -> sign.toLowerCase().contains(query.toLowerCase()))
                        .toList();
                if (!matchingSigns.isEmpty()) {
                    searchResults.put("emergencySigns", matchingSigns);
                }
            }
            
            // Search in recommended foods
            if (guidelines.getRecommendedFoods() != null) {
                var matchingFoods = guidelines.getRecommendedFoods().stream()
                        .filter(food -> food.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                                food.getFoods().stream().anyMatch(f -> f.toLowerCase().contains(query.toLowerCase())))
                        .toList();
                if (!matchingFoods.isEmpty()) {
                    searchResults.put("recommendedFoods", matchingFoods);
                }
            }
            
            // Search in foods to avoid
            if (guidelines.getFoodsToAvoid() != null) {
                var matchingAvoid = guidelines.getFoodsToAvoid().stream()
                        .filter(avoid -> avoid.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                                avoid.getFoods().stream().anyMatch(f -> f.toLowerCase().contains(query.toLowerCase())))
                        .toList();
                if (!matchingAvoid.isEmpty()) {
                    searchResults.put("foodsToAvoid", matchingAvoid);
                }
            }
            
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error searching diet information: " + e.getMessage());
        }
    }
}