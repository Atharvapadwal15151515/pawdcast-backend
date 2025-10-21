package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.PetHealth;
import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.service.PetHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pet-health")
@CrossOrigin(origins = "*")
public class PetHealthController {

    @Autowired
    private PetHealthService petHealthService;

    @PostMapping("/add")
    public ResponseEntity<String> addPetHealth(
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate nextVetDate
    ) {
        petHealthService.savePetHealth(
                petId, weight, height, dietNotes, medicalConditions, vaccinationRecords,
                lastVetVisit, nextVetVisit, medications, activityLevel, entryDate,
                exerciseNotes, medicalNotes, nextVetDate
        );

        return ResponseEntity.ok("Pet health record saved successfully!");
    }

    // ✅ New endpoint: Get pets of a specific user
    @GetMapping("/pets/{userId}")
    public ResponseEntity<List<PetProfile>> getPetsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(petHealthService.getPetsByOwnerId(userId));
    }

    // ✅ New endpoint: Get health records for a specific pet
    @GetMapping("/records/{petId}")
    public ResponseEntity<List<PetHealth>> getPetHealthRecords(@PathVariable Integer petId) {
        List<PetHealth> records = petHealthService.getHealthRecordsByPetId(petId);
        return ResponseEntity.ok(records);
    }
}