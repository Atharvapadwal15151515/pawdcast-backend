package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.PetDietGuidelines;
import com.pawdcast.pawdcast.application.model.PetDietTips;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class PetDietService {
    
    private final ObjectMapper objectMapper;
    
    public PetDietService() {
        this.objectMapper = new ObjectMapper();
    }
    
    public PetDietTips getPetDietTips() {
        try {
            ClassPathResource resource = new ClassPathResource("pet_diet_tips.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, PetDietTips.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load pet diet tips", e);
        }
    }
    
    public PetDietGuidelines getPetDietGuidelines() {
        try {
            ClassPathResource resource = new ClassPathResource("pet_diet_guidelines.json");
            InputStream inputStream = resource.getInputStream();
            return objectMapper.readValue(inputStream, PetDietGuidelines.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load pet diet guidelines", e);
        }
    }
    
    public PetDietTips getFilteredTips(String category) {
        PetDietTips allTips = getPetDietTips();
        
        if (category == null || category.isEmpty()) {
            return allTips;
        }
        
        PetDietTips filteredTips = new PetDietTips();
        
        switch (category.toLowerCase()) {
            case "general":
                filteredTips.setGeneralTips(allTips.getGeneralTips());
                break;
            case "special":
                filteredTips.setSpecialConsiderations(allTips.getSpecialConsiderations());
                break;
            case "treats":
                filteredTips.setTreatGuidelines(allTips.getTreatGuidelines());
                break;
            case "emergency":
                filteredTips.setEmergencySigns(allTips.getEmergencySigns());
                break;
            default:
                return allTips;
        }
        
        return filteredTips;
    }
    
    public PetDietGuidelines getFilteredGuidelines(String category) {
        PetDietGuidelines allGuidelines = getPetDietGuidelines();
        
        if (category == null || category.isEmpty()) {
            return allGuidelines;
        }
        
        PetDietGuidelines filteredGuidelines = new PetDietGuidelines();
        
        switch (category.toLowerCase()) {
            case "recommended":
                filteredGuidelines.setRecommendedFoods(allGuidelines.getRecommendedFoods());
                break;
            case "avoid":
                filteredGuidelines.setFoodsToAvoid(allGuidelines.getFoodsToAvoid());
                break;
            case "feeding":
                filteredGuidelines.setFeedingGuidelines(allGuidelines.getFeedingGuidelines());
                break;
            default:
                return allGuidelines;
        }
        
        return filteredGuidelines;
    }
    
    public Object getTipsByPetType(String petType) {
        PetDietTips allTips = getPetDietTips();
        PetDietGuidelines allGuidelines = getPetDietGuidelines();
        
        // Create a combined response for specific pet types
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        
        if (petType.equalsIgnoreCase("puppy") || petType.equalsIgnoreCase("kitten")) {
            response.put("specialConsiderations", allTips.getSpecialConsiderations().stream()
                    .filter(sc -> sc.getCategory().equalsIgnoreCase("Puppies/Kittens"))
                    .findFirst()
                    .orElse(null));
            response.put("feedingGuidelines", allGuidelines.getFeedingGuidelines().getPuppiesKittens());
            
        } else if (petType.equalsIgnoreCase("senior")) {
            response.put("specialConsiderations", allTips.getSpecialConsiderations().stream()
                    .filter(sc -> sc.getCategory().equalsIgnoreCase("Senior Pets"))
                    .findFirst()
                    .orElse(null));
            response.put("feedingGuidelines", allGuidelines.getFeedingGuidelines().getSeniorPets());
            
        } else if (petType.equalsIgnoreCase("dog")) {
            response.put("feedingGuidelines", allGuidelines.getFeedingGuidelines().getAdultDogs());
            response.put("generalTips", allTips.getGeneralTips());
            
        } else if (petType.equalsIgnoreCase("cat")) {
            response.put("feedingGuidelines", allGuidelines.getFeedingGuidelines().getAdultCats());
            response.put("generalTips", allTips.getGeneralTips());
        }
        
        return response;
    }
}