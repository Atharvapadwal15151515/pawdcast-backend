package com.pawdcast.pawdcast.application.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pawdcast.pawdcast.application.model.Breed;
import com.pawdcast.pawdcast.application.repository.BreedRepository;

@Service
public class BreedService {
    
    @Autowired
    private BreedRepository breedRepository;
    
    // Get all breeds
    public List<Breed> getAllBreeds() {
        return breedRepository.findAll();
    }
    
    // Get breeds by animal type
    public List<Breed> getBreedsByAnimalType(String animalType) {
        return breedRepository.findByAnimalType(animalType);
    }
    
    // Get breed by ID
    public Breed getBreedById(Long id) {
        return breedRepository.findById(id).orElse(null);
    }
    
    // Search breeds by name
    public List<Breed> searchBreeds(String searchTerm) {
        return breedRepository.findByBreedNameContainingIgnoreCase(searchTerm);
    }
    
    // Advanced breed recommendation using Map
    public List<Breed> getRecommendedBreeds(Map<String, Object> criteria) {
        String animalType = (String) criteria.get("animalType");
        String idealHome = (String) criteria.get("idealHome");
        String activityLevel = (String) criteria.get("activityLevel");
        Boolean allergyFriendly = (Boolean) criteria.get("allergyFriendly");
        String careTime = (String) criteria.get("careTime");
        String climatePreference = (String) criteria.get("climatePreference");
        
        return breedRepository.findRecommendedBreeds(
            animalType, idealHome, activityLevel, allergyFriendly, careTime, climatePreference
        );
    }
    
    // Simple recommendation based on home size and activity
    public List<Breed> getSimpleRecommendation(String animalType, String idealHome, String activityLevel) {
        return breedRepository.findByAnimalTypeAndIdealHomeAndActivityLevel(
            animalType, idealHome, activityLevel);
    }
    
    // Get allergy-friendly breeds
    public List<Breed> getAllergyFriendlyBreeds(String animalType) {
        return breedRepository.findByAnimalTypeAndAllergyFriendly(animalType, true);
    }
    
    // Get breeds by multiple animal types
    public List<Breed> getBreedsByMultipleTypes(List<String> animalTypes) {
        return breedRepository.findByAnimalTypeIn(animalTypes);
    }
    
    // Save or update breed
    public Breed saveBreed(Breed breed) {
        return breedRepository.save(breed);
    }
    
    // Delete breed
    public void deleteBreed(Long id) {
        breedRepository.deleteById(id);
    }
}