package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "habit_logs")
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    
    @Column(name = "habit_id", nullable = false)
    private Integer habitId;
    
    // ManyToOne relationship with Habit
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", insertable = false, updatable = false)
    private Habit habit;
    
    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;
    
    @Column(name = "status")
    private String status = "Missed";

    // Constructors
    public HabitLog() {}

    public HabitLog(Integer habitId, LocalDate logDate, String status) {
        this.habitId = habitId;
        this.logDate = logDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    
    public Integer getHabitId() { return habitId; }
    public void setHabitId(Integer habitId) { this.habitId = habitId; }
    
    public Habit getHabit() { return habit; }
    public void setHabit(Habit habit) { this.habit = habit; }
    
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}