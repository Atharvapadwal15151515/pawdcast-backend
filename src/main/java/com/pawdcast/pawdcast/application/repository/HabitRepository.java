package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Integer> {
    
    // Find habits by pet ID
    List<Habit> findByPetId(Integer petId);
    
    // Find habits by multiple pet IDs (for user's all pets)
    List<Habit> findByPetIdIn(List<Integer> petIds);
    
    // Find active habits by pet ID and date
    @Query("SELECT h FROM Habit h WHERE h.petId = :petId AND h.startDate <= :date")
    List<Habit> findActiveHabitsByPetIdAndDate(@Param("petId") Integer petId, @Param("date") LocalDate date);
    
    // Find habits with missed days for auto-update
    @Query("SELECT h FROM Habit h WHERE h.lastCompletedDate < :yesterday OR h.lastCompletedDate IS NULL")
    List<Habit> findHabitsWithMissedDays(@Param("yesterday") LocalDate yesterday);
    
    // Find habit with pet profile eager loading
    @Query("SELECT h FROM Habit h JOIN FETCH h.petProfile WHERE h.habitId = :habitId")
    Optional<Habit> findByIdWithPetProfile(@Param("habitId") Integer habitId);
}