package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.FoodEntry;
import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.FoodEntryService;
import com.pawdcast.pawdcast.application.service.PetProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/food-entries")
@CrossOrigin(origins = "*")
public class FoodEntryController {
    
    @Autowired
    private FoodEntryService foodEntryService;
    
    @Autowired
    private PetProfileService petProfileService;
    
    // Create a new food entry
    @PostMapping("/create")
    public ResponseEntity<?> createFoodEntry(
            @RequestParam String petName,
            @RequestParam String foodType,
            @RequestParam Double quantity,
            @RequestParam String unit,
            @RequestParam(required = false) String notes,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime feedingTime,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            // Verify that the pet name belongs to the logged-in user
            if (!petProfileService.existsByOwnerIdAndName(user.getId(), petName)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Pet not found or doesn't belong to your account.");
            }
            
            // Create food entry
            FoodEntry foodEntry = new FoodEntry(petName, foodType, quantity, unit, notes, feedingTime);
            FoodEntry savedEntry = foodEntryService.createFoodEntry(foodEntry);
            
            // Return response
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedEntry.getId());
            response.put("petName", savedEntry.getPetName());
            response.put("foodType", savedEntry.getFoodType());
            response.put("quantity", savedEntry.getQuantity());
            response.put("unit", savedEntry.getUnit());
            response.put("notes", savedEntry.getNotes());
            response.put("feedingTime", savedEntry.getFeedingTime());
            response.put("createdAt", savedEntry.getCreatedAt());
            response.put("message", "Food entry created successfully!");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Get all food entries for logged-in user's pets
    @GetMapping("/my-entries")
    public ResponseEntity<?> getMyFoodEntries(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            List<FoodEntry> foodEntries = foodEntryService.getFoodEntriesByUserPets(user.getId());
            
            // Convert to response DTO
            List<Map<String, Object>> response = foodEntries.stream().map(entry -> {
                Map<String, Object> entryMap = new HashMap<>();
                entryMap.put("id", entry.getId());
                entryMap.put("petName", entry.getPetName());
                entryMap.put("foodType", entry.getFoodType());
                entryMap.put("quantity", entry.getQuantity());
                entryMap.put("unit", entry.getUnit());
                entryMap.put("notes", entry.getNotes());
                entryMap.put("feedingTime", entry.getFeedingTime());
                entryMap.put("createdAt", entry.getCreatedAt());
                return entryMap;
            }).toList();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving food entries: " + e.getMessage());
        }
    }
    
    // Get food entries for a specific pet
    @GetMapping("/pet/{petName}")
    public ResponseEntity<?> getFoodEntriesByPet(@PathVariable String petName, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            List<FoodEntry> foodEntries = foodEntryService.getFoodEntriesByPetName(petName, user.getId());
            
            List<Map<String, Object>> response = foodEntries.stream().map(entry -> {
                Map<String, Object> entryMap = new HashMap<>();
                entryMap.put("id", entry.getId());
                entryMap.put("petName", entry.getPetName());
                entryMap.put("foodType", entry.getFoodType());
                entryMap.put("quantity", entry.getQuantity());
                entryMap.put("unit", entry.getUnit());
                entryMap.put("notes", entry.getNotes());
                entryMap.put("feedingTime", entry.getFeedingTime());
                entryMap.put("createdAt", entry.getCreatedAt());
                return entryMap;
            }).toList();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Get user's pets for dropdown selection
    @GetMapping("/my-pets")
    public ResponseEntity<?> getMyPets(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            List<PetProfile> petProfiles = petProfileService.getPetProfilesByOwnerId(user.getId());
            
            List<Map<String, Object>> response = petProfiles.stream().map(pet -> {
                Map<String, Object> petMap = new HashMap<>();
                petMap.put("name", pet.getName());
                petMap.put("type", pet.getType());
                petMap.put("breed", pet.getBreed());
                return petMap;
            }).toList();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving pets: " + e.getMessage());
        }
    }
    
    // Update food entry
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFoodEntry(
            @PathVariable Long id,
            @RequestParam String petName,
            @RequestParam String foodType,
            @RequestParam Double quantity,
            @RequestParam String unit,
            @RequestParam(required = false) String notes,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime feedingTime,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            // Verify that the new pet name belongs to the user
            if (!petProfileService.existsByOwnerIdAndName(user.getId(), petName)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Pet not found or doesn't belong to your account.");
            }
            
            // Create updated food entry object
            FoodEntry foodEntryUpdates = new FoodEntry();
            foodEntryUpdates.setPetName(petName);
            foodEntryUpdates.setFoodType(foodType);
            foodEntryUpdates.setQuantity(quantity);
            foodEntryUpdates.setUnit(unit);
            foodEntryUpdates.setNotes(notes);
            foodEntryUpdates.setFeedingTime(feedingTime);
            
            FoodEntry updatedEntry = foodEntryService.updateFoodEntry(id, foodEntryUpdates, user.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedEntry.getId());
            response.put("petName", updatedEntry.getPetName());
            response.put("foodType", updatedEntry.getFoodType());
            response.put("quantity", updatedEntry.getQuantity());
            response.put("unit", updatedEntry.getUnit());
            response.put("notes", updatedEntry.getNotes());
            response.put("feedingTime", updatedEntry.getFeedingTime());
            response.put("updatedAt", updatedEntry.getUpdatedAt());
            response.put("message", "Food entry updated successfully!");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Delete food entry
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFoodEntry(@PathVariable Long id, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            foodEntryService.deleteFoodEntry(id, user.getId());
            return ResponseEntity.ok("Food entry deleted successfully");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Get food entries by date range for a specific pet
    @GetMapping("/pet/{petName}/date-range")
    public ResponseEntity<?> getFoodEntriesByDateRange(
            @PathVariable String petName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            List<FoodEntry> foodEntries = foodEntryService.getFoodEntriesByDateRange(
                    petName, user.getId(), startDate, endDate);
            
            List<Map<String, Object>> response = foodEntries.stream().map(entry -> {
                Map<String, Object> entryMap = new HashMap<>();
                entryMap.put("id", entry.getId());
                entryMap.put("petName", entry.getPetName());
                entryMap.put("foodType", entry.getFoodType());
                entryMap.put("quantity", entry.getQuantity());
                entryMap.put("unit", entry.getUnit());
                entryMap.put("notes", entry.getNotes());
                entryMap.put("feedingTime", entry.getFeedingTime());
                return entryMap;
            }).toList();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}