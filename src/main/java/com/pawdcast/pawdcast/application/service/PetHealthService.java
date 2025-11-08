package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.PetHealth;
import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.repository.PetHealthRepository;
import com.pawdcast.pawdcast.application.repository.PetProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PetHealthService {

    @Autowired
    private PetHealthRepository petHealthRepository;

    @Autowired
    private PetProfileRepository petProfileRepository;

    public void savePetHealth(Integer petId, Double weight, Double height, String dietNotes,
                              String medicalConditions, String vaccinationRecords,
                              LocalDate lastVetVisit, LocalDate nextVetVisit, String medications,
                              String activityLevel, LocalDate entryDate, String exerciseNotes,
                              String medicalNotes, LocalDate nextVetDate) {

        PetHealth health = new PetHealth();

        // Fetch pet profile by ID
        PetProfile pet = petProfileRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found with ID: " + petId));

        health.setPet(pet);
        health.setWeight(weight);
        health.setHeight(height);
        health.setDietNotes(dietNotes);
        health.setMedicalConditions(medicalConditions);
        health.setVaccinationRecords(vaccinationRecords);
        health.setLastVetVisit(lastVetVisit);
        health.setNextVetVisit(nextVetVisit);
        health.setMedications(medications);
        health.setActivityLevel(activityLevel);
        health.setEntryDate(entryDate);
        health.setExerciseNotes(exerciseNotes);
        health.setMedicalNotes(medicalNotes);
        health.setNextVetDate(nextVetDate);

        petHealthRepository.save(health);
    }

    // ✅ New method to get pets by owner ID
    public List<PetProfile> getPetsByOwnerId(Integer ownerId) {
        return petProfileRepository.findByOwnerId(ownerId);
    }

    // ✅ New method to get health records by pet ID
    public List<PetHealth> getHealthRecordsByPetId(Integer petId) {
        return petHealthRepository.findByPetPetIdOrderByEntryDateDesc(petId);
    }

    // ✅ New method to check if pet belongs to user
    public boolean isPetOwnedByUser(Integer petId, Integer userId) {
        PetProfile pet = petProfileRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + petId));
        return pet.getOwnerId().equals(userId);
    }

    // ✅ New method to get health records by pet ID with ownership validation
    public List<PetHealth> getHealthRecordsByPetIdAndOwnerId(Integer petId, Integer ownerId) {
        // Verify ownership first
        if (!isPetOwnedByUser(petId, ownerId)) {
            throw new RuntimeException("Access denied - pet does not belong to user");
        }
        return petHealthRepository.findByPetPetIdOrderByEntryDateDesc(petId);
    }
}