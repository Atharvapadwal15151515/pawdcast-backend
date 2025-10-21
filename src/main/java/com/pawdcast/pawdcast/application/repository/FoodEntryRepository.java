package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.FoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    
    // Find all food entries for a specific pet name
    List<FoodEntry> findByPetNameOrderByFeedingTimeDesc(String petName);
    
    // Find food entries for multiple pet names
    List<FoodEntry> findByPetNameInOrderByFeedingTimeDesc(List<String> petNames);
    
    // Find food entries for a specific pet within a date range
    List<FoodEntry> findByPetNameAndFeedingTimeBetweenOrderByFeedingTimeDesc(
            String petName, LocalDateTime start, LocalDateTime end);
    
    // Find all food entries for a user's pets (based on pet names)
    @Query("SELECT f FROM FoodEntry f WHERE f.petName IN :petNames ORDER BY f.feedingTime DESC")
    List<FoodEntry> findByPetNames(@Param("petNames") List<String> petNames);
    
    // Check if a pet name has any food entries
    boolean existsByPetName(String petName);
    
    // Delete all entries for a specific pet name
    void deleteByPetName(String petName);
}