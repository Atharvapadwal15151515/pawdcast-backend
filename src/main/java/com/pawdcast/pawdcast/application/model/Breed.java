package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "breeds")
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "animal_type")
    private String animalType;
    
    @Column(name = "breed_name")
    private String breedName;
    
    @Column(name = "ideal_income")
    private String idealIncome;
    
    @Column(name = "ideal_home")
    private String idealHome;
    
    @Column(name = "activity_level")
    private String activityLevel;
    
    @Column(name = "allergy_friendly")
    private Boolean allergyFriendly;
    
    @Column(name = "care_time")
    private String careTime;
    
    @Column(name = "climate_preference")
    private String climatePreference;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    // Constructors
    public Breed() {}
    
    public Breed(String animalType, String breedName, String idealIncome, String idealHome, 
                 String activityLevel, Boolean allergyFriendly, String careTime, 
                 String climatePreference, String description) {
        this.animalType = animalType;
        this.breedName = breedName;
        this.idealIncome = idealIncome;
        this.idealHome = idealHome;
        this.activityLevel = activityLevel;
        this.allergyFriendly = allergyFriendly;
        this.careTime = careTime;
        this.climatePreference = climatePreference;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAnimalType() { return animalType; }
    public void setAnimalType(String animalType) { this.animalType = animalType; }
    
    public String getBreedName() { return breedName; }
    public void setBreedName(String breedName) { this.breedName = breedName; }
    
    public String getIdealIncome() { return idealIncome; }
    public void setIdealIncome(String idealIncome) { this.idealIncome = idealIncome; }
    
    public String getIdealHome() { return idealHome; }
    public void setIdealHome(String idealHome) { this.idealHome = idealHome; }
    
    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }
    
    public Boolean getAllergyFriendly() { return allergyFriendly; }
    public void setAllergyFriendly(Boolean allergyFriendly) { this.allergyFriendly = allergyFriendly; }
    
    public String getCareTime() { return careTime; }
    public void setCareTime(String careTime) { this.careTime = careTime; }
    
    public String getClimatePreference() { return climatePreference; }
    public void setClimatePreference(String climatePreference) { this.climatePreference = climatePreference; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}