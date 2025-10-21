package com.pawdcast.pawdcast.application.model;

public class TrainingTip {
    private String category;
    private String title;
    private String description;
    private String imageUrl;

    public TrainingTip() {}

    public TrainingTip(String category, String title, String description, String imageUrl) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
