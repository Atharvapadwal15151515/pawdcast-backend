package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pet_habits")
public class Habit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer habitId;
    
    @Column(name = "pet_id", nullable = false)
    private Integer petId;
    
    // ManyToOne relationship with PetProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", insertable = false, updatable = false)
    private PetProfile petProfile;
    
    @Column(name = "habit_name", nullable = false)
    private String habitName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "frequency")
    private String frequency = "Daily";
    
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @Column(name = "streak")
    private Integer streak = 0;
    
    @Column(name = "longest_streak")
    private Integer longestStreak = 0;
    
    @Column(name = "total_completed")
    private Integer totalCompleted = 0;
    
    @Column(name = "total_missed")
    private Integer totalMissed = 0;
    
    @Column(name = "last_completed_date")
    private LocalDate lastCompletedDate;
    
    @Column(name = "creation_timestamp")
    private LocalDateTime creationTimestamp = LocalDateTime.now();

    // Constructors
    public Habit() {}

    public Habit(Integer petId, String habitName, String description, String frequency, LocalDate startDate) {
        this.petId = petId;
        this.habitName = habitName;
        this.description = description;
        this.frequency = frequency;
        this.startDate = startDate;
        this.streak = 0;
        this.longestStreak = 0;
        this.totalCompleted = 0;
        this.totalMissed = 0;
    }

    // Getters and Setters
    public Integer getHabitId() { return habitId; }
    public void setHabitId(Integer habitId) { this.habitId = habitId; }
    
    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }
    
    public PetProfile getPetProfile() { return petProfile; }
    public void setPetProfile(PetProfile petProfile) { this.petProfile = petProfile; }
    
    public String getHabitName() { return habitName; }
    public void setHabitName(String habitName) { this.habitName = habitName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public Integer getStreak() { return streak; }
    public void setStreak(Integer streak) { this.streak = streak; }
    
    public Integer getLongestStreak() { return longestStreak; }
    public void setLongestStreak(Integer longestStreak) { this.longestStreak = longestStreak; }
    
    public Integer getTotalCompleted() { return totalCompleted; }
    public void setTotalCompleted(Integer totalCompleted) { this.totalCompleted = totalCompleted; }
    
    public Integer getTotalMissed() { return totalMissed; }
    public void setTotalMissed(Integer totalMissed) { this.totalMissed = totalMissed; }
    
    public LocalDate getLastCompletedDate() { return lastCompletedDate; }
    public void setLastCompletedDate(LocalDate lastCompletedDate) { this.lastCompletedDate = lastCompletedDate; }
    
    public LocalDateTime getCreationTimestamp() { return creationTimestamp; }
    public void setCreationTimestamp(LocalDateTime creationTimestamp) { this.creationTimestamp = creationTimestamp; }

    // Helper methods
    public Double getCompletionRate() {
        int total = totalCompleted + totalMissed;
        return total > 0 ? (double) totalCompleted / total * 100 : 0.0;
    }
}