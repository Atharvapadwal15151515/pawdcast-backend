package com.pawdcast.pawdcast.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pawdcast.pawdcast.application.model.Breed;

@Repository
public interface BreedRepository extends JpaRepository<Breed, Long> {
    
    // Find by animal type
    List<Breed> findByAnimalType(String animalType);
    
    // Find by multiple criteria
    List<Breed> findByAnimalTypeAndIdealHomeAndActivityLevel(
        String animalType, String idealHome, String activityLevel);
    
    // Find allergy friendly breeds
    List<Breed> findByAnimalTypeAndAllergyFriendly(String animalType, Boolean allergyFriendly);
    
    // Custom query for recommendation engine
    @Query("SELECT b FROM Breed b WHERE " +
           "b.animalType = :animalType AND " +
           "(:idealHome IS NULL OR b.idealHome = :idealHome) AND " +
           "(:activityLevel IS NULL OR b.activityLevel = :activityLevel) AND " +
           "(:allergyFriendly IS NULL OR b.allergyFriendly = :allergyFriendly) AND " +
           "(:careTime IS NULL OR b.careTime = :careTime) AND " +
           "(:climatePreference IS NULL OR b.climatePreference = :climatePreference)")
    List<Breed> findRecommendedBreeds(
        @Param("animalType") String animalType,
        @Param("idealHome") String idealHome,
        @Param("activityLevel") String activityLevel,
        @Param("allergyFriendly") Boolean allergyFriendly,
        @Param("careTime") String careTime,
        @Param("climatePreference") String climatePreference);
    
    // Find by breed name containing (search functionality)
    List<Breed> findByBreedNameContainingIgnoreCase(String breedName);
    
    // Find by multiple animal types
    List<Breed> findByAnimalTypeIn(List<String> animalTypes);
}