package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.FoodEntry;
import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.repository.FoodEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FoodEntryService {
    
    @Autowired
    private FoodEntryRepository foodEntryRepository;
    
    @Autowired
    private PetProfileService petProfileService;
    
    // Create a new food entry
    public FoodEntry createFoodEntry(FoodEntry foodEntry) {
        // Validate that the pet name exists for the user (this will be handled in controller)
        return foodEntryRepository.save(foodEntry);
    }
    
    // Get all food entries for a specific user's pets
    public List<FoodEntry> getFoodEntriesByUserPets(Integer userId) {
        // First, get all pet names for this user
        List<PetProfile> userPets = petProfileService.getPetProfilesByOwnerId(userId);
        List<String> petNames = userPets.stream()
                .map(PetProfile::getName)
                .toList();
        
        return foodEntryRepository.findByPetNames(petNames);
    }
    
    // Get food entries for a specific pet (by name) for a user
    public List<FoodEntry> getFoodEntriesByPetName(String petName, Integer userId) {
        // Verify that the pet belongs to the user
        if (!petProfileService.existsByOwnerIdAndName(userId, petName)) {
            throw new RuntimeException("Pet not found or doesn't belong to user");
        }
        
        return foodEntryRepository.findByPetNameOrderByFeedingTimeDesc(petName);
    }
    
    // Get food entry by ID with user validation
    public Optional<FoodEntry> getFoodEntryById(Long id, Integer userId) {
        Optional<FoodEntry> foodEntry = foodEntryRepository.findById(id);
        
        if (foodEntry.isPresent()) {
            // Verify that the pet in this food entry belongs to the user
            String petName = foodEntry.get().getPetName();
            if (!petProfileService.existsByOwnerIdAndName(userId, petName)) {
                throw new RuntimeException("You don't have permission to access this food entry");
            }
        }
        
        return foodEntry;
    }
    
    // Update food entry
    public FoodEntry updateFoodEntry(Long id, FoodEntry foodEntryUpdates, Integer userId) {
        Optional<FoodEntry> existingEntry = getFoodEntryById(id, userId);
        
        if (existingEntry.isEmpty()) {
            throw new RuntimeException("Food entry not found");
        }
        
        FoodEntry existing = existingEntry.get();
        
        // Verify that the updated pet name still belongs to the user
        if (!foodEntryUpdates.getPetName().equals(existing.getPetName())) {
            if (!petProfileService.existsByOwnerIdAndName(userId, foodEntryUpdates.getPetName())) {
                throw new RuntimeException("Pet not found or doesn't belong to user");
            }
        }
        
        // Update fields
        existing.setPetName(foodEntryUpdates.getPetName());
        existing.setFoodType(foodEntryUpdates.getFoodType());
        existing.setQuantity(foodEntryUpdates.getQuantity());
        existing.setUnit(foodEntryUpdates.getUnit());
        existing.setNotes(foodEntryUpdates.getNotes());
        existing.setFeedingTime(foodEntryUpdates.getFeedingTime());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return foodEntryRepository.save(existing);
    }
    
    // Delete food entry
    public void deleteFoodEntry(Long id, Integer userId) {
        Optional<FoodEntry> foodEntry = getFoodEntryById(id, userId);
        
        if (foodEntry.isEmpty()) {
            throw new RuntimeException("Food entry not found");
        }
        
        foodEntryRepository.deleteById(id);
    }
    
    // Get food entries for date range for a specific pet
    public List<FoodEntry> getFoodEntriesByDateRange(String petName, Integer userId, 
                                                    LocalDateTime start, LocalDateTime end) {
        // Verify that the pet belongs to the user
        if (!petProfileService.existsByOwnerIdAndName(userId, petName)) {
            throw new RuntimeException("Pet not found or doesn't belong to user");
        }
        
        return foodEntryRepository.findByPetNameAndFeedingTimeBetweenOrderByFeedingTimeDesc(
                petName, start, end);
    }
    
    // Check if user has any food entries for their pets
    public boolean hasFoodEntries(Integer userId) {
        List<PetProfile> userPets = petProfileService.getPetProfilesByOwnerId(userId);
        List<String> petNames = userPets.stream()
                .map(PetProfile::getName)
                .toList();
        
        for (String petName : petNames) {
            if (foodEntryRepository.existsByPetName(petName)) {
                return true;
            }
        }
        return false;
    }
}