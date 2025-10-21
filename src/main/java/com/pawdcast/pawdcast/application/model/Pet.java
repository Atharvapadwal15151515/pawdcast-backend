package com.pawdcast.pawdcast.application.model;

import java.util.List;

public class Pet {
    private String name;
    private String origin;
    private String size;
    private String lifespan;
    private List<String> temperament;
    private String description;
    private String image_url;
    private String type; // "dog" or "cat" to differentiate

    // Default constructor
    public Pet() {}

    // All-args constructor
    public Pet(String name, String origin, String size, String lifespan,
               List<String> temperament, String description, String image_url, String type) {
        this.name = name;
        this.origin = origin;
        this.size = size;
        this.lifespan = lifespan;
        this.temperament = temperament;
        this.description = description;
        this.image_url = image_url;
        this.type = type;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getLifespan() { return lifespan; }
    public void setLifespan(String lifespan) { this.lifespan = lifespan; }

    public List<String> getTemperament() { return temperament; }
    public void setTemperament(List<String> temperament) { this.temperament = temperament; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}