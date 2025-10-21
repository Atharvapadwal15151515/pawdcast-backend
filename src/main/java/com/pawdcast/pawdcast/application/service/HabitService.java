package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.Habit;
import com.pawdcast.pawdcast.application.model.HabitLog;
import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.repository.HabitRepository;
import com.pawdcast.pawdcast.application.repository.HabitLogRepository;
import com.pawdcast.pawdcast.application.repository.PetProfileRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;
    
    @Autowired
    private HabitLogRepository habitLogRepository;
    
    @Autowired
    private PetProfileRepository petProfileRepository;

    // Helper method to get current user ID from session
    private Integer getCurrentUserId(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        return user.getId();
    }

    // Helper method to get user's pet IDs
    private List<Integer> getUserPetIds(HttpSession session) {
        Integer userId = getCurrentUserId(session);
        List<PetProfile> userPets = petProfileRepository.findByOwnerId(userId);
        return userPets.stream()
                .map(PetProfile::getPetId)
                .collect(Collectors.toList());
    }

    // 1️⃣ Add Habit with session validation
    public Habit addHabit(Habit habit, HttpSession session) {
        Integer userId = getCurrentUserId(session);
        
        // Verify that the pet belongs to the current user
        Optional<PetProfile> pet = petProfileRepository.findById(habit.getPetId());
        if (pet.isEmpty() || !pet.get().getOwnerId().equals(userId)) {
            throw new RuntimeException("Pet not found or doesn't belong to user");
        }
        
        habit.setStreak(0);
        habit.setLongestStreak(0);
        habit.setTotalCompleted(0);
        habit.setTotalMissed(0);
        return habitRepository.save(habit);
    }

    // 2️⃣ Mark Habit as Completed with session validation
    public boolean markHabitAsCompleted(Integer habitId, HttpSession session) {
        Integer userId = getCurrentUserId(session);
        
        Optional<Habit> habitOpt = habitRepository.findById(habitId);
        if (habitOpt.isEmpty()) {
            return false;
        }

        Habit habit = habitOpt.get();
        
        // Verify that the habit's pet belongs to the current user
        Optional<PetProfile> pet = petProfileRepository.findById(habit.getPetId());
        if (pet.isEmpty() || !pet.get().getOwnerId().equals(userId)) {
            throw new RuntimeException("Habit doesn't belong to user");
        }

        LocalDate today = LocalDate.now();

        // Check if already completed today
        Optional<HabitLog> existingLog = habitLogRepository.findByHabitIdAndLogDate(habitId, today);
        if (existingLog.isPresent() && "Completed".equals(existingLog.get().getStatus())) {
            return false; // Already completed today
        }

        // Update or create habit log
        HabitLog log = existingLog.orElse(new HabitLog(habitId, today, "Completed"));
        log.setStatus("Completed");
        habitLogRepository.save(log);

        // Update habit stats
        updateHabitStats(habit, today);
        
        return true;
    }

    // 3️⃣ Auto Update Stats (same as before)
    private void updateHabitStats(Habit habit, LocalDate completionDate) {
        boolean continuesStreak = habit.getLastCompletedDate() != null &&
                                 habit.getLastCompletedDate().equals(completionDate.minusDays(1));
        
        if (continuesStreak) {
            habit.setStreak(habit.getStreak() + 1);
        } else {
            habit.setStreak(1);
        }
        
        if (habit.getStreak() > habit.getLongestStreak()) {
            habit.setLongestStreak(habit.getStreak());
        }
        
        habit.setTotalCompleted(habit.getTotalCompleted() + 1);
        habit.setLastCompletedDate(completionDate);
        
        habitRepository.save(habit);
    }

    // 4️⃣ Get user's pets for selection
    public List<PetProfile> getUserPets(HttpSession session) {
        Integer userId = getCurrentUserId(session);
        return petProfileRepository.findByOwnerId(userId);
    }

    // 5️⃣ Get habits for a specific pet with validation
    public List<Habit> getHabitsByPetId(Integer petId, HttpSession session) {
        Integer userId = getCurrentUserId(session);
        
        // Verify that the pet belongs to the current user
        Optional<PetProfile> pet = petProfileRepository.findById(petId);
        if (pet.isEmpty() || !pet.get().getOwnerId().equals(userId)) {
            throw new RuntimeException("Pet not found or doesn't belong to user");
        }
        
        return habitRepository.findByPetId(petId);
    }

    // 6️⃣ Get all habits for current user (all pets)
    public List<Habit> getUserHabits(HttpSession session) {
        List<Integer> userPetIds = getUserPetIds(session);
        if (userPetIds.isEmpty()) {
            return List.of();
        }
        return habitRepository.findByPetIdIn(userPetIds);
    }

    // 7️⃣ Get Calendar Data with validation
    public Map<LocalDate, String> getHabitCalendar(Integer habitId, int year, int month, HttpSession session) {
        Integer userId = getCurrentUserId(session);
        
        Optional<Habit> habitOpt = habitRepository.findById(habitId);
        if (habitOpt.isEmpty()) {
            throw new RuntimeException("Habit not found");
        }
        
        // Verify that the habit's pet belongs to the current user
        Habit habit = habitOpt.get();
        Optional<PetProfile> pet = petProfileRepository.findById(habit.getPetId());
        if (pet.isEmpty() || !pet.get().getOwnerId().equals(userId)) {
            throw new RuntimeException("Habit doesn't belong to user");
        }

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        List<HabitLog> logs = habitLogRepository.findByHabitIdAndLogDateBetween(habitId, startDate, endDate);
        
        Map<LocalDate, String> calendarData = new HashMap<>();
        for (HabitLog log : logs) {
            calendarData.put(log.getLogDate(), log.getStatus());
        }
        
        return calendarData;
    }

    // 8️⃣ Analytics with session validation
    public Map<String, Object> getHabitAnalytics(Integer habitId, HttpSession session) {
        Integer userId = getCurrentUserId(session);
        
        Optional<Habit> habitOpt = habitRepository.findById(habitId);
        if (habitOpt.isEmpty()) {
            return Map.of("error", "Habit not found");
        }
        
        // Verify ownership
        Habit habit = habitOpt.get();
        Optional<PetProfile> pet = petProfileRepository.findById(habit.getPetId());
        if (pet.isEmpty() || !pet.get().getOwnerId().equals(userId)) {
            throw new RuntimeException("Habit doesn't belong to user");
        }

        Map<String, Object> analytics = new HashMap<>();
        analytics.put("completionRate", habit.getCompletionRate());
        analytics.put("currentStreak", habit.getStreak());
        analytics.put("longestStreak", habit.getLongestStreak());
        analytics.put("totalCompleted", habit.getTotalCompleted());
        analytics.put("totalMissed", habit.getTotalMissed());
        
        return analytics;
    }

    public Map<String, Object> getUserAnalytics(HttpSession session) {
        List<Habit> userHabits = getUserHabits(session);
        
        Map<String, Object> analytics = new HashMap<>();
        double totalCompletionRate = 0;
        int habitCount = userHabits.size();
        int highestStreak = 0;
        Habit mostConsistentHabit = null;
        double highestCompletionRate = 0;

        for (Habit habit : userHabits) {
            double completionRate = habit.getCompletionRate();
            totalCompletionRate += completionRate;
            
            if (habit.getLongestStreak() > highestStreak) {
                highestStreak = habit.getLongestStreak();
            }
            
            if (completionRate > highestCompletionRate) {
                highestCompletionRate = completionRate;
                mostConsistentHabit = habit;
            }
        }

        analytics.put("averageCompletionRate", habitCount > 0 ? totalCompletionRate / habitCount : 0);
        analytics.put("totalHabits", habitCount);
        analytics.put("highestStreak", highestStreak);
        analytics.put("mostConsistentHabit", mostConsistentHabit != null ? 
            mostConsistentHabit.getHabitName() : "No habits");
        analytics.put("highestCompletionRate", highestCompletionRate);

        return analytics;
    }

    // 9️⃣ Get recent activity for habit detail
    public List<HabitLog> getRecentActivity(Integer habitId, HttpSession session) {
        Integer userId = getCurrentUserId(session);
        
        Optional<Habit> habitOpt = habitRepository.findById(habitId);
        if (habitOpt.isEmpty()) {
            throw new RuntimeException("Habit not found");
        }
        
        // Verify ownership
        Habit habit = habitOpt.get();
        Optional<PetProfile> pet = petProfileRepository.findById(habit.getPetId());
        if (pet.isEmpty() || !pet.get().getOwnerId().equals(userId)) {
            throw new RuntimeException("Habit doesn't belong to user");
        }
        
        return habitLogRepository.findRecentActivityByHabitId(habitId);
    }

    // Scheduled task to mark missed habits
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkMissedHabits() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Habit> habitsToCheck = habitRepository.findHabitsWithMissedDays(yesterday);
        
        for (Habit habit : habitsToCheck) {
            if (shouldHabitBeCompleted(habit, yesterday)) {
                markHabitAsMissed(habit, yesterday);
            }
        }
    }

    private boolean shouldHabitBeCompleted(Habit habit, LocalDate date) {
        return !date.isBefore(habit.getStartDate()) && "Daily".equals(habit.getFrequency());
    }

    private void markHabitAsMissed(Habit habit, LocalDate date) {
        Optional<HabitLog> existingLog = habitLogRepository.findByHabitIdAndLogDate(habit.getHabitId(), date);
        
        if (existingLog.isEmpty()) {
            HabitLog missedLog = new HabitLog(habit.getHabitId(), date, "Missed");
            habitLogRepository.save(missedLog);
            
            habit.setTotalMissed(habit.getTotalMissed() + 1);
            
            if (habit.getLastCompletedDate() != null && 
                habit.getLastCompletedDate().equals(date.minusDays(1))) {
                habit.setStreak(0);
            }
            
            habitRepository.save(habit);
        }
    }
}