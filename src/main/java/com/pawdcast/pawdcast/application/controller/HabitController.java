package com.pawdcast.pawdcast.application.controller;

import com.pawdcast.pawdcast.application.model.Habit;
import com.pawdcast.pawdcast.application.model.HabitLog;
import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.HabitService;
import com.pawdcast.pawdcast.application.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habits")
@CrossOrigin(origins = "*")
public class HabitController {

    @Autowired
    private HabitService habitService;

    @Autowired
    private AuthService authService;

    // Helper method to get current user
    private User getCurrentUser(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            throw new RuntimeException("User not authenticated");
        }
        return authService.findByEmail(userEmail);
    }

    // 1️⃣ Get user's pets for selection
    @GetMapping("/pets")
    public ResponseEntity<?> getUserPets(HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            List<PetProfile> pets = habitService.getUserPets(user.getId());
            return ResponseEntity.ok(pets);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 2️⃣ Add Habit
    @PostMapping
    public ResponseEntity<?> addHabit(@RequestBody Habit habit, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            Habit savedHabit = habitService.addHabit(habit, user.getId());
            return ResponseEntity.ok(savedHabit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 3️⃣ Mark Habit as Completed
    @PostMapping("/{habitId}/complete")
    public ResponseEntity<?> markHabitAsCompleted(@PathVariable Integer habitId, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            boolean success = habitService.markHabitAsCompleted(habitId, user.getId());
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Habit marked as completed"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to mark habit as completed"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 4️⃣ Get Habits by Pet
    @GetMapping("/pet/{petId}")
    public ResponseEntity<?> getHabitsByPet(@PathVariable Integer petId, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            List<Habit> habits = habitService.getHabitsByPetId(petId, user.getId());
            return ResponseEntity.ok(habits);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 5️⃣ Get All User Habits (across all pets)
    @GetMapping("/user")
    public ResponseEntity<?> getUserHabits(HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            List<Habit> habits = habitService.getUserHabits(user.getId());
            return ResponseEntity.ok(habits);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
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
            HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            Map<LocalDate, String> calendarData = habitService.getHabitCalendar(habitId, year, month, user.getId());
            return ResponseEntity.ok(calendarData);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 7️⃣ Get Habit Analytics
    @GetMapping("/{habitId}/analytics")
    public ResponseEntity<?> getHabitAnalytics(@PathVariable Integer habitId, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            Map<String, Object> analytics = habitService.getHabitAnalytics(habitId, user.getId());
            return ResponseEntity.ok(analytics);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 8️⃣ Get User Analytics
    @GetMapping("/user/analytics")
    public ResponseEntity<?> getUserAnalytics(HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            Map<String, Object> analytics = habitService.getUserAnalytics(user.getId());
            return ResponseEntity.ok(analytics);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 9️⃣ Get Recent Activity for Habit
    @GetMapping("/{habitId}/recent-activity")
    public ResponseEntity<?> getRecentActivity(@PathVariable Integer habitId, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            List<HabitLog> recentActivity = habitService.getRecentActivity(habitId, user.getId());
            return ResponseEntity.ok(recentActivity);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 🔟 Get specific habit
    @GetMapping("/{habitId}")
    public ResponseEntity<?> getHabit(@PathVariable Integer habitId, HttpServletRequest request) {
        try {
            User user = getCurrentUser(request);
            Habit habit = habitService.getHabitById(habitId, user.getId());
            return ResponseEntity.ok(habit);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}