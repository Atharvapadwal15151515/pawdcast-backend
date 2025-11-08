package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.PetHealth;
import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.PetHealthService;
import com.pawdcast.pawdcast.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pet-health")
@CrossOrigin(origins = "*")
public class PetHealthController {

    @Autowired
    private PetHealthService petHealthService;

    @Autowired
    private AuthService authService;

    private User getCurrentUser(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            throw new RuntimeException("User not authenticated");
        }
        return authService.findByEmail(userEmail);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPetHealth(
            @RequestParam Integer petId,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) Double height,
            @RequestParam(required = false) String dietNotes,
            @RequestParam(required = false) String medicalConditions,
            @RequestParam(required = false) String vaccinationRecords,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastVetVisit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nextVetVisit,
            @RequestParam(required = false) String medications,
            @RequestParam(required = false) String activityLevel,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate,
            @RequestParam(required = false) String exerciseNotes,
            @RequestParam(required = false) String medicalNotes,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nextVetDate,
            HttpServletRequest request
    ) {
        try {
            User user = getCurrentUser(request);
            
            // Verify that the pet belongs to the user
            if (!petHealthService.isPetOwnedByUser(petId, user.getId())) {
                return ResponseEntity.status(403).body("Access denied - pet does not belong to user");
            }
            
            petHealthService.savePetHealth(
                    petId, weight, height, dietNotes, medicalConditions, vaccinationRecords,
                    lastVetVisit, nextVetVisit, medications, activityLevel, entryDate,
                    exerciseNotes, medicalNotes, nextVetDate
            );

            return ResponseEntity.ok("Pet health record saved successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving health record: " + e.getMessage());
        }
    }

    // Get pets of the current logged-in user
    @GetMapping("/my-pets")
    public ResponseEntity<?> getMyPets(HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            List<PetProfile> pets = petHealthService.getPetsByOwnerId(user.getId());
            return ResponseEntity.ok(pets);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving pets: " + e.getMessage());
        }
    }

    // Get health records for a specific pet (with ownership validation)
    @GetMapping("/records/{petId}")
    public ResponseEntity<?> getPetHealthRecords(@PathVariable Integer petId, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            
            // Verify that the pet belongs to the user
            if (!petHealthService.isPetOwnedByUser(petId, user.getId())) {
                return ResponseEntity.status(403).body("Access denied - pet does not belong to user");
            }
            
            List<PetHealth> records = petHealthService.getHealthRecordsByPetId(petId);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving health records: " + e.getMessage());
        }
    }
}