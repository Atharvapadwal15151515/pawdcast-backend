package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    
    List<HabitLog> findByHabitId(Integer habitId);
    
    List<HabitLog> findByHabitIdAndLogDateBetween(Integer habitId, LocalDate startDate, LocalDate endDate);
    
    Optional<HabitLog> findByHabitIdAndLogDate(Integer habitId, LocalDate logDate);
    
    @Query("SELECT hl FROM HabitLog hl WHERE hl.habitId IN :habitIds AND hl.logDate BETWEEN :startDate AND :endDate")
    List<HabitLog> findByHabitIdsAndDateRange(@Param("habitIds") List<Integer> habitIds, 
                                             @Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);
    
    @Query("SELECT COUNT(hl) FROM HabitLog hl WHERE hl.habitId = :habitId AND hl.status = 'Completed'")
    Long countCompletedByHabitId(@Param("habitId") Integer habitId);
    
    // Get recent activity for a habit
    @Query("SELECT hl FROM HabitLog hl WHERE hl.habitId = :habitId ORDER BY hl.logDate DESC LIMIT 7")
    List<HabitLog> findRecentActivityByHabitId(@Param("habitId") Integer habitId);
}