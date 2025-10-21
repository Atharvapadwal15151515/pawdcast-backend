package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.Diary;
import com.pawdcast.pawdcast.application.repository.DiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    public Diary createDiaryEntry(Integer userId, LocalDate entryDate, 
                                 String content, String mood, String activity, 
                                 String location, String tags, String weather, 
                                 Integer rating, Boolean shared, Boolean reminder,
                                 String entryTitle, MultipartFile image) throws IOException {
        
        // Debug logging to verify content is received
        System.out.println("=== SERVICE: Creating Diary ===");
        System.out.println("User ID: " + userId);
        System.out.println("Entry Date: " + entryDate);
        System.out.println("Content received: " + (content != null ? content.substring(0, Math.min(100, content.length())) + "..." : "NULL"));
        System.out.println("Entry Title: " + entryTitle);
        System.out.println("Mood: " + mood);
        System.out.println("Activity: " + activity);
        
        Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setEntryDate(entryDate);
        diary.setContent(content);  // Make sure content is set
        diary.setMood(mood);
        diary.setActivity(activity);
        diary.setLocation(location);
        diary.setTags(tags);
        diary.setWeather(weather);
        diary.setRating(rating);
        diary.setShared(shared != null ? shared : false);
        diary.setReminder(reminder != null ? reminder : false);
        diary.setEntryTitle(entryTitle);

        // Handle image upload - now using only photo field
        if (image != null && !image.isEmpty()) {
            System.out.println("Processing image: " + image.getOriginalFilename());
            diary.setImageFromBytes(image.getBytes());
        }

        Diary savedDiary = diaryRepository.save(diary);
        
        // Verify content was saved
        System.out.println("=== SERVICE: Diary Saved Successfully ===");
        System.out.println("Diary ID: " + savedDiary.getDiaryId());
        System.out.println("Saved content: " + (savedDiary.getContent() != null ? 
            "Present (" + savedDiary.getContent().length() + " characters)" : "NULL"));
        System.out.println("Saved entry title: " + savedDiary.getEntryTitle());
        
        return savedDiary;
    }

    public Diary createDiaryEntry(Integer userId, LocalDate entryDate, String content) {
        System.out.println("=== SERVICE: Creating Simple Diary ===");
        System.out.println("Content: " + (content != null ? content.substring(0, Math.min(100, content.length())) + "..." : "NULL"));
        
        Diary diary = new Diary(userId, entryDate, content);
        Diary savedDiary = diaryRepository.save(diary);
        
        System.out.println("Simple diary saved with ID: " + savedDiary.getDiaryId());
        return savedDiary;
    }

    public List<Diary> getUserDiaries(Integer userId) {
        List<Diary> diaries = diaryRepository.findByUserIdOrderByEntryDateDesc(userId);
        
        // Debug: Check if content is loaded
        System.out.println("=== SERVICE: Loading User Diaries ===");
        System.out.println("User ID: " + userId);
        System.out.println("Found " + diaries.size() + " diaries");
        
        for (Diary diary : diaries) {
            System.out.println("Diary ID: " + diary.getDiaryId() + 
                             ", Entry Title: " + diary.getEntryTitle() +
                             ", Content: " + (diary.getContent() != null ? 
                                 "Present (" + diary.getContent().length() + " chars)" : "NULL") +
                             ", Date: " + diary.getEntryDate());
        }
        
        return diaries;
    }

    public Optional<Diary> getDiaryById(Long diaryId) {
        Optional<Diary> diary = diaryRepository.findById(diaryId);
        
        if (diary.isPresent()) {
            System.out.println("=== SERVICE: Get Diary By ID ===");
            System.out.println("Diary ID: " + diaryId);
            System.out.println("Content: " + (diary.get().getContent() != null ? 
                "Present (" + diary.get().getContent().length() + " chars)" : "NULL"));
        }
        
        return diary;
    }

    public Optional<Diary> getDiaryByIdAndUserId(Long diaryId, Integer userId) {
        Optional<Diary> diary = diaryRepository.findByDiaryIdAndUserId(diaryId, userId);
        
        // Debug specific diary retrieval
        if (diary.isPresent()) {
            System.out.println("=== SERVICE: Get Specific Diary ===");
            System.out.println("Diary ID: " + diaryId + ", User ID: " + userId);
            System.out.println("Entry Title: " + diary.get().getEntryTitle());
            System.out.println("Content: " + (diary.get().getContent() != null ? 
                "Present (" + diary.get().getContent().length() + " chars)" : "NULL"));
            System.out.println("Full Content Preview: " + 
                (diary.get().getContent() != null ? 
                 diary.get().getContent().substring(0, Math.min(100, diary.get().getContent().length())) + "..." : "NULL"));
        } else {
            System.out.println("=== SERVICE: Diary Not Found ===");
            System.out.println("Diary ID: " + diaryId + ", User ID: " + userId + " not found");
        }
        
        return diary;
    }

    public Diary updateDiaryEntry(Long diaryId, Integer userId, LocalDate entryDate, 
                                 String content, String mood, String activity, 
                                 String location, String tags, String weather, 
                                 Integer rating, Boolean shared, Boolean reminder, 
                                 String entryTitle, MultipartFile image) throws IOException {
        
        System.out.println("=== SERVICE: Updating Diary ===");
        System.out.println("Diary ID: " + diaryId + ", User ID: " + userId);
        System.out.println("New Content: " + (content != null ? content.substring(0, Math.min(100, content.length())) + "..." : "NULL"));
        
        Optional<Diary> existingDiary = diaryRepository.findByDiaryIdAndUserId(diaryId, userId);
        if (existingDiary.isPresent()) {
            Diary diary = existingDiary.get();
            
            System.out.println("Existing Content: " + (diary.getContent() != null ? 
                "Present (" + diary.getContent().length() + " chars)" : "NULL"));
            
            diary.setEntryDate(entryDate);
            diary.setContent(content);  // Ensure content is updated
            diary.setMood(mood);
            diary.setActivity(activity);
            diary.setLocation(location);
            diary.setTags(tags);
            diary.setWeather(weather);
            diary.setRating(rating);
            diary.setShared(shared != null ? shared : false);
            diary.setReminder(reminder != null ? reminder : false);
            diary.setEntryTitle(entryTitle);
            diary.setUpdatedAt(LocalDateTime.now());

            // Update image if provided - now using only photo field
            if (image != null && !image.isEmpty()) {
                System.out.println("Updating image: " + image.getOriginalFilename());
                diary.setImageFromBytes(image.getBytes());
            }

            Diary updatedDiary = diaryRepository.save(diary);
            
            // Debug: Verify update
            System.out.println("=== SERVICE: Diary Updated Successfully ===");
            System.out.println("Updated content: " + (updatedDiary.getContent() != null ? 
                "Present (" + updatedDiary.getContent().length() + " chars)" : "NULL"));
            System.out.println("Updated entry title: " + updatedDiary.getEntryTitle());
            
            return updatedDiary;
        }
        throw new RuntimeException("Diary entry not found or user mismatch");
    }

    public void deleteDiaryEntry(Long diaryId, Integer userId) {
        System.out.println("=== SERVICE: Deleting Diary ===");
        System.out.println("Diary ID: " + diaryId + ", User ID: " + userId);
        
        if (!diaryRepository.existsByDiaryIdAndUserId(diaryId, userId)) {
            throw new RuntimeException("Diary entry not found or user mismatch");
        }
        diaryRepository.deleteByDiaryIdAndUserId(diaryId, userId);
        
        System.out.println("Diary deleted successfully");
    }

    public List<Diary> searchDiaries(Integer userId, String searchTerm) {
        System.out.println("=== SERVICE: Searching Diaries ===");
        System.out.println("User ID: " + userId + ", Search Term: " + searchTerm);
        
        List<Diary> results = diaryRepository.searchInUserDiaries(userId, searchTerm);
        
        System.out.println("Found " + results.size() + " results");
        for (Diary diary : results) {
            System.out.println("Result - ID: " + diary.getDiaryId() + 
                             ", Title: " + diary.getEntryTitle() +
                             ", Content: " + (diary.getContent() != null ? "Present" : "NULL"));
        }
        
        return results;
    }

    public List<Diary> getDiariesByTag(Integer userId, String tag) {
        return diaryRepository.findByUserIdAndTagsContainingIgnoreCaseOrderByEntryDateDesc(userId, tag);
    }

    public List<Diary> getDiariesByDateRange(Integer userId, LocalDate startDate, LocalDate endDate) {
        System.out.println("=== SERVICE: Getting Diaries by Date Range ===");
        System.out.println("User ID: " + userId + ", From: " + startDate + " To: " + endDate);
        
        List<Diary> diaries = diaryRepository.findByUserIdAndEntryDateBetweenOrderByEntryDateDesc(userId, startDate, endDate);
        
        System.out.println("Found " + diaries.size() + " diaries in date range");
        return diaries;
    }

    public List<Diary> getDiariesByMood(Integer userId, String mood) {
        return diaryRepository.findByUserIdAndMoodOrderByEntryDateDesc(userId, mood);
    }

    public List<Diary> getDiariesWithHighRating(Integer userId, Integer minRating) {
        return diaryRepository.findByUserIdAndRatingGreaterThanEqualOrderByEntryDateDesc(userId, minRating);
    }

    public List<Diary> getSharedDiaries(Integer userId) {
        return diaryRepository.findByUserIdAndSharedTrueOrderByEntryDateDesc(userId);
    }

    public List<Diary> getDiariesWithImages(Integer userId) {
        return diaryRepository.findUserDiariesWithImages(userId);
    }

    public List<Diary> getRecentDiaries(Integer userId, int limit) {
        List<Diary> diaries = diaryRepository.findTop5ByUserIdOrderByEntryDateDesc(userId);
        
        System.out.println("=== SERVICE: Getting Recent Diaries ===");
        System.out.println("Found " + diaries.size() + " recent diaries");
        
        return diaries;
    }

    public List<Diary> getDiariesByYear(Integer userId, int year) {
        return diaryRepository.findByUserIdAndYear(userId, year);
    }

    public List<Diary> getDiariesByMonth(Integer userId, int year, int month) {
        return diaryRepository.findByUserIdAndYearAndMonth(userId, year, month);
    }

    public boolean isEntryDateAvailable(Integer userId, LocalDate entryDate) {
        boolean available = !diaryRepository.existsByUserIdAndEntryDate(userId, entryDate);
        
        System.out.println("=== SERVICE: Checking Date Availability ===");
        System.out.println("User ID: " + userId + ", Date: " + entryDate + ", Available: " + available);
        
        return available;
    }

    public boolean userOwnsDiary(Long diaryId, Integer userId) {
        return diaryRepository.existsByDiaryIdAndUserId(diaryId, userId);
    }

    public long getDiaryCount(Integer userId) {
        return diaryRepository.countByUserId(userId);
    }

    public long getDiaryCountWithImages(Integer userId) {
        return diaryRepository.countUserDiariesWithImages(userId);
    }

    public List<Diary> getDiariesBySpecificDate(Integer userId, LocalDate entryDate) {
        return diaryRepository.findByUserIdAndEntryDateOrderByCreatedAtDesc(userId, entryDate);
    }
}