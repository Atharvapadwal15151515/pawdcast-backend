package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    
    // Get all diaries for user ordered by date (NEWEST FIRST)
    List<Diary> findByUserIdOrderByEntryDateDesc(Integer userId);
    
    // Get diaries for user within date range
    List<Diary> findByUserIdAndEntryDateBetweenOrderByEntryDateDesc(
        Integer userId, LocalDate startDate, LocalDate endDate);
    
    // Get diaries by tag (case-insensitive)
    List<Diary> findByUserIdAndTagsContainingIgnoreCaseOrderByEntryDateDesc(
        Integer userId, String tag);
    
    // COMPREHENSIVE SEARCH across all text fields including CONTENT
    @Query("SELECT d FROM Diary d WHERE d.userId = :userId AND " +
           "(LOWER(d.entryTitle) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.tags) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.activity) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.mood) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(d.weather) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY d.entryDate DESC")
    List<Diary> searchInUserDiaries(@Param("userId") Integer userId, 
                                   @Param("searchTerm") String searchTerm);
    
    // Check if entry exists for user on specific date
    boolean existsByUserIdAndEntryDate(Integer userId, LocalDate entryDate);
    
    // Find diary by ID and user ID (security check)
    Optional<Diary> findByDiaryIdAndUserId(Long diaryId, Integer userId);
    
    // Get diaries by mood
    List<Diary> findByUserIdAndMoodOrderByEntryDateDesc(Integer userId, String mood);
    
    // Get diaries with minimum rating
    List<Diary> findByUserIdAndRatingGreaterThanEqualOrderByEntryDateDesc(
        Integer userId, Integer minRating);
    
    // Get shared diaries
    List<Diary> findByUserIdAndSharedTrueOrderByEntryDateDesc(Integer userId);
    
    // Get diaries with images (using photo field)
    @Query("SELECT d FROM Diary d WHERE d.userId = :userId AND d.photo IS NOT NULL ORDER BY d.entryDate DESC")
    List<Diary> findUserDiariesWithImages(@Param("userId") Integer userId);
    
    // Get diaries for specific date
    List<Diary> findByUserIdAndEntryDateOrderByCreatedAtDesc(
        Integer userId, LocalDate entryDate);
    
    // Count total diaries for user
    long countByUserId(Integer userId);
    
    // Count diaries with images for user
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.userId = :userId AND d.photo IS NOT NULL")
    long countUserDiariesWithImages(@Param("userId") Integer userId);
    
    // Get recent diaries (limited)
    List<Diary> findTop5ByUserIdOrderByEntryDateDesc(Integer userId);
    
    // Get diaries by year
    @Query("SELECT d FROM Diary d WHERE d.userId = :userId AND YEAR(d.entryDate) = :year ORDER BY d.entryDate DESC")
    List<Diary> findByUserIdAndYear(@Param("userId") Integer userId, @Param("year") int year);
    
    // Get diaries by year and month
    @Query("SELECT d FROM Diary d WHERE d.userId = :userId AND YEAR(d.entryDate) = :year AND MONTH(d.entryDate) = :month ORDER BY d.entryDate DESC")
    List<Diary> findByUserIdAndYearAndMonth(@Param("userId") Integer userId, 
                                           @Param("year") int year, 
                                           @Param("month") int month);
    
    // Delete diary by ID and user ID (security)
    void deleteByDiaryIdAndUserId(Long diaryId, Integer userId);
    
    // Check if diary exists for user (security)
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Diary d WHERE d.diaryId = :diaryId AND d.userId = :userId")
    boolean existsByDiaryIdAndUserId(@Param("diaryId") Long diaryId, @Param("userId") Integer userId);
    
    // Get diaries with content (for verification)
    @Query("SELECT d FROM Diary d WHERE d.userId = :userId AND d.content IS NOT NULL AND LENGTH(TRIM(d.content)) > 0 ORDER BY d.entryDate DESC")
    List<Diary> findUserDiariesWithContent(@Param("userId") Integer userId);
    
    // Count diaries with content
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.userId = :userId AND d.content IS NOT NULL AND LENGTH(TRIM(d.content)) > 0")
    long countUserDiariesWithContent(@Param("userId") Integer userId);
    
    // Get diaries by entry title
    List<Diary> findByUserIdAndEntryTitleContainingIgnoreCaseOrderByEntryDateDesc(
        Integer userId, String entryTitle);
    
    // Get diaries with specific activity
    List<Diary> findByUserIdAndActivityContainingIgnoreCaseOrderByEntryDateDesc(
        Integer userId, String activity);
    
    // Get diaries with specific location
    List<Diary> findByUserIdAndLocationContainingIgnoreCaseOrderByEntryDateDesc(
        Integer userId, String location);
    
    // Get diaries by weather condition
    List<Diary> findByUserIdAndWeatherContainingIgnoreCaseOrderByEntryDateDesc(
        Integer userId, String weather);
    
    // Get diaries with reminders
    List<Diary> findByUserIdAndReminderTrueOrderByEntryDateDesc(Integer userId);
    
    // Get diaries created after specific date
    List<Diary> findByUserIdAndCreatedAtAfterOrderByCreatedAtDesc(
        Integer userId, LocalDate createdAfter);
    
    // Get diaries updated after specific date
    List<Diary> findByUserIdAndUpdatedAtAfterOrderByUpdatedAtDesc(
        Integer userId, LocalDate updatedAfter);
    
    // Find duplicate entries for same date (for data cleanup)
    @Query("SELECT d FROM Diary d WHERE d.userId = :userId AND d.entryDate = :entryDate AND d.diaryId != :excludeDiaryId")
    List<Diary> findOtherEntriesForSameDate(@Param("userId") Integer userId, 
                                           @Param("entryDate") LocalDate entryDate,
                                           @Param("excludeDiaryId") Long excludeDiaryId);
    
    // Get diary statistics by month for a year
    @Query("SELECT MONTH(d.entryDate) as month, COUNT(d) as count FROM Diary d WHERE d.userId = :userId AND YEAR(d.entryDate) = :year GROUP BY MONTH(d.entryDate) ORDER BY month")
    List<Object[]> getMonthlyDiaryCounts(@Param("userId") Integer userId, @Param("year") int year);
    
    // Get most used moods
    @Query("SELECT d.mood, COUNT(d) as count FROM Diary d WHERE d.userId = :userId AND d.mood IS NOT NULL GROUP BY d.mood ORDER BY count DESC")
    List<Object[]> getMoodStatistics(@Param("userId") Integer userId);
    
    // Get most used activities
    @Query("SELECT d.activity, COUNT(d) as count FROM Diary d WHERE d.userId = :userId AND d.activity IS NOT NULL GROUP BY d.activity ORDER BY count DESC")
    List<Object[]> getActivityStatistics(@Param("userId") Integer userId);
    
    // Get average rating by month
    @Query("SELECT MONTH(d.entryDate) as month, AVG(d.rating) as avgRating FROM Diary d WHERE d.userId = :userId AND d.rating IS NOT NULL AND YEAR(d.entryDate) = :year GROUP BY MONTH(d.entryDate) ORDER BY month")
    List<Object[]> getMonthlyAverageRatings(@Param("userId") Integer userId, @Param("year") int year);
}