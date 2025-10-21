package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Habit;
import com.pawdcast.pawdcast.application.model.HabitLog;
import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.HabitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = "*")
public class HabitController {

    @Autowired
    private HabitService habitService;

    // Helper method to check authentication
    private ResponseEntity<?> checkAuthentication(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
        }
        return null;
    }

    // 1️⃣ Get user's pets for selection
    @GetMapping("/pets")
    public ResponseEntity<?> getUserPets(HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            List<PetProfile> pets = habitService.getUserPets(session);
            return ResponseEntity.ok(pets);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2️⃣ Add Habit
    @PostMapping
    public ResponseEntity<?> addHabit(@RequestBody Habit habit, HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            Habit savedHabit = habitService.addHabit(habit, session);
            return ResponseEntity.ok(savedHabit);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 3️⃣ Mark Habit as Completed
    @PostMapping("/{habitId}/complete")
    public ResponseEntity<?> markHabitAsCompleted(@PathVariable Integer habitId, HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            boolean success = habitService.markHabitAsCompleted(habitId, session);
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Habit marked as completed"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to mark habit as completed"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 4️⃣ Get Habits by Pet
    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getHabitsByPet(@PathVariable Integer petId, HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            List<Habit> habits = habitService.getHabitsByPetId(petId, session);
            return ResponseEntity.ok(habits);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 5️⃣ Get All User Habits (across all pets)
    @GetMapping("/user")
    public ResponseEntity<?> getUserHabits(HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            List<Habit> habits = habitService.getUserHabits(session);
            return ResponseEntity.ok(habits);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 6️⃣ Get Calendar View
    @GetMapping("/{habitId}/calendar/{year}/{month}")
    public ResponseEntity<?> getHabitCalendar(
            @PathVariable Integer habitId,
            @PathVariable int year,
            @PathVariable int month,
            HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            Map<LocalDate, String> calendarData = habitService.getHabitCalendar(habitId, year, month, session);
            return ResponseEntity.ok(calendarData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 7️⃣ Get Habit Analytics
    @GetMapping("/{habitId}/analytics")
    public ResponseEntity<?> getHabitAnalytics(@PathVariable Integer habitId, HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            Map<String, Object> analytics = habitService.getHabitAnalytics(habitId, session);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 8️⃣ Get User Analytics
    @GetMapping("/user/analytics")
    public ResponseEntity<?> getUserAnalytics(HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            Map<String, Object> analytics = habitService.getUserAnalytics(session);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 9️⃣ Get Recent Activity for Habit
    @GetMapping("/{habitId}/recent-activity")
    public ResponseEntity<?> getRecentActivity(@PathVariable Integer habitId, HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            List<HabitLog> recentActivity = habitService.getRecentActivity(habitId, session);
            return ResponseEntity.ok(recentActivity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 🔟 Get specific habit
    @GetMapping("/{habitId}")
    public ResponseEntity<?> getHabit(@PathVariable Integer habitId, HttpSession session) {
        ResponseEntity<?> authCheck = checkAuthentication(session);
        if (authCheck != null) return authCheck;

        try {
            // This would need a similar service method with validation
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}