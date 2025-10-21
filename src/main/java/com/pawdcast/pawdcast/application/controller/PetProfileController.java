package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.PetProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pet-profiles")
@CrossOrigin(origins = "*")
public class PetProfileController {
    
    @Autowired
    private PetProfileService petProfileService;
    
    // Create Pet Profile
    @PostMapping("/create")
    public ResponseEntity<?> createPetProfile(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String breed,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
            @RequestParam(required = false) String medicalRecords,
            @RequestParam(required = false) MultipartFile photo,
            HttpSession session) {
        
        try {
            // Get logged-in user from session
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            // Process photo
            byte[] photoBytes = null;
            if (photo != null && !photo.isEmpty()) {
                photoBytes = photo.getBytes();
            }
            
            // Create pet profile object
            PetProfile petProfile = new PetProfile(
                user.getId(),  // Automatically get ownerId from logged-in user
                name,
                type,
                breed,
                dob,
                medicalRecords,
                photoBytes
            );
            
            // Save pet profile
            PetProfile savedPetProfile = petProfileService.createPetProfile(petProfile);
            
            // Return response with pet profile details (excluding photo for list view)
            Map<String, Object> response = new HashMap<>();
            response.put("petId", savedPetProfile.getPetId());
            response.put("name", savedPetProfile.getName());
            response.put("type", savedPetProfile.getType());
            response.put("breed", savedPetProfile.getBreed());
            response.put("dob", savedPetProfile.getDob());
            response.put("medicalRecords", savedPetProfile.getMedicalRecords());
            response.put("createdAt", savedPetProfile.getCreatedAt());
            response.put("hasPhoto", savedPetProfile.getPhoto() != null);
            response.put("message", "Pet profile created successfully!");
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("Error processing uploaded photo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Get all pet profiles for logged-in user
    @GetMapping("/my-pets")
    public ResponseEntity<?> getMyPetProfiles(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            List<PetProfile> petProfiles = petProfileService.getPetProfilesByOwnerId(user.getId());
            
            // Create response without photo bytes to reduce payload size
            List<Map<String, Object>> response = petProfiles.stream().map(petProfile -> {
                Map<String, Object> petMap = new HashMap<>();
                petMap.put("petId", petProfile.getPetId());
                petMap.put("name", petProfile.getName());
                petMap.put("type", petProfile.getType());
                petMap.put("breed", petProfile.getBreed());
                petMap.put("dob", petProfile.getDob());
                petMap.put("medicalRecords", petProfile.getMedicalRecords());
                petMap.put("createdAt", petProfile.getCreatedAt());
                petMap.put("hasPhoto", petProfile.getPhoto() != null);
                return petMap;
            }).toList();
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving pet profiles: " + e.getMessage());
        }
    }
    
    // Get pet profile photo
    @GetMapping("/{petId}/photo")
    public ResponseEntity<byte[]> getPetProfilePhoto(@PathVariable Integer petId, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // Verify the pet profile belongs to the logged-in user
            if (!petProfileService.isPetProfileOwner(petId, user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            
            PetProfile petProfile = petProfileService.getPetProfileById(petId)
                    .orElseThrow(() -> new RuntimeException("Pet profile not found"));
            
            if (petProfile.getPhoto() == null) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg") // Adjust based on your image type
                    .body(petProfile.getPhoto());
            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Get specific pet profile details
    @GetMapping("/{petId}")
    public ResponseEntity<?> getPetProfileDetails(@PathVariable Integer petId, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            // Verify the pet profile belongs to the logged-in user
            if (!petProfileService.isPetProfileOwner(petId, user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to access this pet profile");
            }
            
            PetProfile petProfile = petProfileService.getPetProfileById(petId)
                    .orElseThrow(() -> new RuntimeException("Pet profile not found"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("petId", petProfile.getPetId());
            response.put("name", petProfile.getName());
            response.put("type", petProfile.getType());
            response.put("breed", petProfile.getBreed());
            response.put("dob", petProfile.getDob());
            response.put("medicalRecords", petProfile.getMedicalRecords());
            response.put("createdAt", petProfile.getCreatedAt());
            response.put("hasPhoto", petProfile.getPhoto() != null);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Update pet profile
    @PutMapping("/{petId}")
    public ResponseEntity<?> updatePetProfile(
            @PathVariable Integer petId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
            @RequestParam(required = false) String medicalRecords,
            @RequestParam(required = false) MultipartFile photo,
            HttpSession session) {
        
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            // Verify the pet profile belongs to the logged-in user
            if (!petProfileService.isPetProfileOwner(petId, user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to update this pet profile");
            }
            
            // Create pet profile object with updated fields
            PetProfile petProfileUpdates = new PetProfile();
            petProfileUpdates.setName(name);
            petProfileUpdates.setType(type);
            petProfileUpdates.setBreed(breed);
            petProfileUpdates.setDob(dob);
            petProfileUpdates.setMedicalRecords(medicalRecords);
            
            // Process photo if provided
            if (photo != null && !photo.isEmpty()) {
                petProfileUpdates.setPhoto(photo.getBytes());
            }
            
            PetProfile updatedPetProfile = petProfileService.updatePetProfile(petId, petProfileUpdates);
            
            Map<String, Object> response = new HashMap<>();
            response.put("petId", updatedPetProfile.getPetId());
            response.put("name", updatedPetProfile.getName());
            response.put("type", updatedPetProfile.getType());
            response.put("breed", updatedPetProfile.getBreed());
            response.put("dob", updatedPetProfile.getDob());
            response.put("medicalRecords", updatedPetProfile.getMedicalRecords());
            response.put("createdAt", updatedPetProfile.getCreatedAt());
            response.put("hasPhoto", updatedPetProfile.getPhoto() != null);
            response.put("message", "Pet profile updated successfully!");
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("Error processing uploaded photo: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Delete pet profile
    @DeleteMapping("/{petId}")
    public ResponseEntity<?> deletePetProfile(@PathVariable Integer petId, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            // Verify the pet profile belongs to the logged-in user
            if (!petProfileService.isPetProfileOwner(petId, user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You don't have permission to delete this pet profile");
            }
            
            petProfileService.deletePetProfile(petId);
            return ResponseEntity.ok("Pet profile deleted successfully");
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Check if pet name already exists for current user
    @GetMapping("/check-name")
    public ResponseEntity<?> checkPetNameAvailability(@RequestParam String name, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not logged in. Please login first.");
            }
            
            boolean exists = petProfileService.existsByOwnerIdAndName(user.getId(), name);
            
            Map<String, Object> response = new HashMap<>();
            response.put("available", !exists);
            response.put("message", exists ? 
                "Pet name already exists for your account" : 
                "Pet name is available");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error checking pet name availability");
        }
    }
}