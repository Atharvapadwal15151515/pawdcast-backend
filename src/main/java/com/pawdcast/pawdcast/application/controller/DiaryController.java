package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Diary;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.DiaryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/diary")
@CrossOrigin(origins = "*")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    private Integer getUserIdFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not authenticated. Please log in.");
        }
        return user.getId();
    }

    // Create new diary entry - PERFECTED VERSION
    @PostMapping("/create")
    public ResponseEntity<?> createDiaryEntry(
            HttpSession session,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate,
            @RequestParam String content, // MAIN CONTENT - This is mandatory
            @RequestParam(required = false) String entryTitle, // Use entryTitle instead of title
            @RequestParam(required = false) String mood,
            @RequestParam(required = false) String activity,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String weather,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false, defaultValue = "false") Boolean shared,
            @RequestParam(required = false, defaultValue = "false") Boolean reminder,
            @RequestParam(required = false) MultipartFile image) {
        
        try {
            Integer userId = getUserIdFromSession(session);

            // VALIDATION: Ensure content is provided
            if (content == null || content.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Diary content cannot be empty");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // VALIDATION: Check if entry date is available
            if (!diaryService.isEntryDateAvailable(userId, entryDate)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "A diary entry already exists for this date");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            System.out.println("=== CONTROLLER: Creating Diary Entry ===");
            System.out.println("User ID: " + userId);
            System.out.println("Entry Date: " + entryDate);
            System.out.println("Entry Title: " + entryTitle);
            System.out.println("Content Length: " + (content != null ? content.length() : 0));
            System.out.println("Content Preview: " + (content != null ? content.substring(0, Math.min(100, content.length())) + "..." : "NULL"));

            Diary diary = diaryService.createDiaryEntry(
                userId, entryDate, content, mood, activity, 
                location, tags, weather, rating, shared, reminder,
                entryTitle, image
            );

            // SUCCESS RESPONSE
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Diary entry created successfully");
            response.put("diaryId", diary.getDiaryId());
            response.put("entryDate", diary.getEntryDate());
            response.put("entryTitle", diary.getEntryTitle());
            response.put("contentPreview", diary.getContent() != null ? 
                diary.getContent().substring(0, Math.min(100, diary.getContent().length())) + "..." : "");
            response.put("hasImage", diary.hasImage());

            System.out.println("=== CONTROLLER: Diary Created Successfully ===");
            System.out.println("Saved Diary ID: " + diary.getDiaryId());
            System.out.println("Final Content Length: " + (diary.getContent() != null ? diary.getContent().length() : 0));

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("Authentication error: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error creating diary entry: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to create diary entry: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get all diary entries for user - PERFECTED VERSION
    @GetMapping("/entries")
    public ResponseEntity<?> getUserDiaries(HttpSession session) {
        try {
            Integer userId = getUserIdFromSession(session);
            
            System.out.println("=== CONTROLLER: Fetching User Diaries ===");
            System.out.println("User ID: " + userId);

            List<Diary> diaries = diaryService.getUserDiaries(userId);
            
            // ENHANCED RESPONSE WITH CONTENT VERIFICATION
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Diaries retrieved successfully");
            response.put("count", diaries.size());
            response.put("diaries", diaries);
            
            // Content verification summary
            long diariesWithContent = diaries.stream()
                .filter(diary -> diary.getContent() != null && !diary.getContent().trim().isEmpty())
                .count();
            response.put("diariesWithContent", diariesWithContent);
            
            System.out.println("=== CONTROLLER: Diaries Retrieved ===");
            System.out.println("Total diaries: " + diaries.size());
            System.out.println("Diaries with content: " + diariesWithContent);
            
            if (diaries.size() > 0) {
                Diary firstDiary = diaries.get(0);
                System.out.println("First diary content: " + 
                    (firstDiary.getContent() != null ? "Present (" + firstDiary.getContent().length() + " chars)" : "NULL"));
            }

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            System.err.println("Authentication error in get entries: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error fetching diaries: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to fetch diaries: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get specific diary entry - PERFECTED VERSION
    @GetMapping("/entry/{diaryId}")
    public ResponseEntity<?> getDiaryEntry(@PathVariable Long diaryId, HttpSession session) {
        try {
            Integer userId = getUserIdFromSession(session);
            
            System.out.println("=== CONTROLLER: Fetching Specific Diary ===");
            System.out.println("Diary ID: " + diaryId + ", User ID: " + userId);

            Optional<Diary> diary = diaryService.getDiaryByIdAndUserId(diaryId, userId);
            
            if (diary.isPresent()) {
                Diary diaryEntry = diary.get();
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Diary entry retrieved successfully");
                response.put("diary", diaryEntry);
                response.put("contentLength", diaryEntry.getContent() != null ? diaryEntry.getContent().length() : 0);
                response.put("hasImage", diaryEntry.hasImage());
                
                System.out.println("=== CONTROLLER: Diary Found ===");
                System.out.println("Content: " + (diaryEntry.getContent() != null ? "PRESENT" : "MISSING"));
                System.out.println("Content Length: " + (diaryEntry.getContent() != null ? diaryEntry.getContent().length() : 0));

                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Diary entry not found or access denied");
                return ResponseEntity.status(404).body(errorResponse);
            }
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error fetching diary entry: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to fetch diary entry: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get diary entry image - PERFECTED VERSION
    @GetMapping("/entry/{diaryId}/image")
    public ResponseEntity<byte[]> getDiaryImage(@PathVariable Long diaryId, HttpSession session) {
        try {
            Integer userId = getUserIdFromSession(session);
            Optional<Diary> diary = diaryService.getDiaryByIdAndUserId(diaryId, userId);
            
            if (diary.isPresent() && diary.get().hasImage()) {
                byte[] imageBytes = diary.get().getPhoto(); // Using photo field now
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);
                headers.setContentLength(imageBytes.length);
                headers.set("Content-Disposition", "inline; filename=\"diary-image.jpg\"");
                return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    // Update diary entry - PERFECTED VERSION
    @PutMapping("/entry/{diaryId}")
    public ResponseEntity<?> updateDiaryEntry(
            @PathVariable Long diaryId,
            HttpSession session,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate,
            @RequestParam String content, // MAIN CONTENT - Mandatory
            @RequestParam(required = false) String entryTitle,
            @RequestParam(required = false) String mood,
            @RequestParam(required = false) String activity,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String weather,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false, defaultValue = "false") Boolean shared,
            @RequestParam(required = false, defaultValue = "false") Boolean reminder,
            @RequestParam(required = false) MultipartFile image) {
        
        try {
            Integer userId = getUserIdFromSession(session);

            // VALIDATION: Ensure content is provided
            if (content == null || content.trim().isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Diary content cannot be empty");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            System.out.println("=== CONTROLLER: Updating Diary Entry ===");
            System.out.println("Diary ID: " + diaryId + ", User ID: " + userId);
            System.out.println("New Content Length: " + content.length());
            System.out.println("Content Preview: " + content.substring(0, Math.min(100, content.length())) + "...");

            Diary updatedDiary = diaryService.updateDiaryEntry(
                diaryId, userId, entryDate, content, mood, activity,
                location, tags, weather, rating, shared, reminder,
                entryTitle, image
            );

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Diary entry updated successfully");
            response.put("diaryId", updatedDiary.getDiaryId());
            response.put("entryDate", updatedDiary.getEntryDate());
            response.put("entryTitle", updatedDiary.getEntryTitle());
            response.put("contentLength", updatedDiary.getContent() != null ? updatedDiary.getContent().length() : 0);
            response.put("hasImage", updatedDiary.hasImage());

            System.out.println("=== CONTROLLER: Diary Updated Successfully ===");
            System.out.println("Final Content Length: " + (updatedDiary.getContent() != null ? updatedDiary.getContent().length() : 0));

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Diary entry not found: " + e.getMessage());
                return ResponseEntity.status(404).body(errorResponse);
            }
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error updating diary entry: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to update diary entry: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Delete diary entry - PERFECTED VERSION
    @DeleteMapping("/entry/{diaryId}")
    public ResponseEntity<?> deleteDiaryEntry(@PathVariable Long diaryId, HttpSession session) {
        try {
            Integer userId = getUserIdFromSession(session);
            
            System.out.println("=== CONTROLLER: Deleting Diary Entry ===");
            System.out.println("Diary ID: " + diaryId + ", User ID: " + userId);

            diaryService.deleteDiaryEntry(diaryId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Diary entry deleted successfully");
            response.put("deletedDiaryId", diaryId);

            System.out.println("=== CONTROLLER: Diary Deleted Successfully ===");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Diary entry not found: " + e.getMessage());
                return ResponseEntity.status(404).body(errorResponse);
            }
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error deleting diary entry: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Failed to delete diary entry: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Search diary entries - PERFECTED VERSION
    @GetMapping("/search")
    public ResponseEntity<?> searchDiaries(
            HttpSession session,
            @RequestParam String query) {
        try {
            Integer userId = getUserIdFromSession(session);
            
            System.out.println("=== CONTROLLER: Searching Diaries ===");
            System.out.println("User ID: " + userId + ", Query: " + query);

            List<Diary> results = diaryService.searchDiaries(userId, query);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Search completed successfully");
            response.put("query", query);
            response.put("results", results);
            response.put("count", results.size());

            System.out.println("=== CONTROLLER: Search Completed ===");
            System.out.println("Found " + results.size() + " results");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error searching diaries: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Search failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Check if entry date is available - PERFECTED VERSION
    @GetMapping("/check-date")
    public ResponseEntity<?> checkDateAvailability(
            HttpSession session,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entryDate) {
        try {
            Integer userId = getUserIdFromSession(session);
            
            System.out.println("=== CONTROLLER: Checking Date Availability ===");
            System.out.println("User ID: " + userId + ", Date: " + entryDate);

            boolean isAvailable = diaryService.isEntryDateAvailable(userId, entryDate);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("available", isAvailable);
            response.put("entryDate", entryDate);
            response.put("message", isAvailable ? "Date is available" : "Date is not available");

            System.out.println("=== CONTROLLER: Date Check Completed ===");
            System.out.println("Available: " + isAvailable);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        } catch (Exception e) {
            System.err.println("Error checking date availability: " + e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Date check failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Get user diary statistics - NEW ENDPOINT
    @GetMapping("/statistics")
    public ResponseEntity<?> getDiaryStatistics(HttpSession session) {
        try {
            Integer userId = getUserIdFromSession(session);
            
            long totalDiaries = diaryService.getDiaryCount(userId);
            long diariesWithImages = diaryService.getDiaryCountWithImages(userId);
            List<Diary> recentDiaries = diaryService.getRecentDiaries(userId, 5);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalDiaries", totalDiaries);
            response.put("diariesWithImages", diariesWithImages);
            response.put("recentDiariesCount", recentDiaries.size());
            response.put("message", "Statistics retrieved successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}