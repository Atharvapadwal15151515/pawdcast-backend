package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pet_diary")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long diaryId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(columnDefinition = "TEXT")
    private String content;
    
    private String mood;
    private String activity;
    private String location;
    private String tags;
    private String weather;
    private Integer rating;
    
    @Column(name = "shared")
    private Boolean shared = false;
    
    @Column(name = "reminder")
    private Boolean reminder;
    
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] photo;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "entry_title")
    private String entryTitle;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Diary() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.shared = false;
    }

    public Diary(Integer userId, LocalDate entryDate, String content) {
        this();
        this.userId = userId;
        this.entryDate = entryDate;
        this.content = content;
    }

    public Diary(Integer userId, LocalDate entryDate, String entryTitle, String content) {
        this();
        this.userId = userId;
        this.entryDate = entryDate;
        this.entryTitle = entryTitle;
        this.content = content;
    }

    // Helper method to handle image upload
    public void setImageFromBytes(byte[] imageBytes) {
        this.photo = imageBytes;
        this.updatedAt = LocalDateTime.now();
    }

    // Helper method to check if diary has image
    public boolean hasImage() {
        return this.photo != null && this.photo.length > 0;
    }

    // Getters and Setters
    public Long getDiaryId() {
        return diaryId;
    }

    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Boolean getShared() {
        return shared;
    }

    public void setShared(Boolean shared) {
        this.shared = shared;
    }

    public Boolean getReminder() {
        return reminder;
    }

    public void setReminder(Boolean reminder) {
        this.reminder = reminder;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEntryTitle() {
        return entryTitle;
    }

    public void setEntryTitle(String entryTitle) {
        this.entryTitle = entryTitle;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "Diary{" +
                "diaryId=" + diaryId +
                ", userId=" + userId +
                ", entryDate=" + entryDate +
                ", entryTitle='" + entryTitle + '\'' +
                ", content='" + (content != null ? content.substring(0, Math.min(50, content.length())) + "..." : "null") + '\'' +
                ", mood='" + mood + '\'' +
                ", rating=" + rating +
                ", createdAt=" + createdAt +
                '}';
    }
}