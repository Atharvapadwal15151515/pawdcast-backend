package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_entries")
public class FoodEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "pet_name", nullable = false)
    private String petName;
    
    @Column(name = "food_type", nullable = false)
    private String foodType;
    
    @Column(nullable = false)
    private Double quantity;
    
    @Column(nullable = false)
    private String unit;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "feeding_time", nullable = false)
    private LocalDateTime feedingTime;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    // Default constructor
    public FoodEntry() {
    }
    
    // Constructor without id for creating new entries
    public FoodEntry(String petName, String foodType, Double quantity, String unit, 
                    String notes, LocalDateTime feedingTime) {
        this.petName = petName;
        this.foodType = foodType;
        this.quantity = quantity;
        this.unit = unit;
        this.notes = notes;
        this.feedingTime = feedingTime;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPetName() {
        return petName;
    }
    
    public void setPetName(String petName) {
        this.petName = petName;
    }
    
    public String getFoodType() {
        return foodType;
    }
    
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
    
    public Double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getFeedingTime() {
        return feedingTime;
    }
    
    public void setFeedingTime(LocalDateTime feedingTime) {
        this.feedingTime = feedingTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // PrePersist and PreUpdate methods
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}