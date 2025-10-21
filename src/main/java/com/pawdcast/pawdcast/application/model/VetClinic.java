package com.pawdcast.pawdcast.application.model; 
 
public class VetClinic { 
    private int id; 
    private String name; 
    private String area; 
    private String locality; 
    private double latitude; 
    private double longitude; 
 
    // No-arg constructor needed for JSON mapping 
    public VetClinic() {} 
 
    public VetClinic(int id, String name, String area, String locality, 
double latitude, double longitude) { 
        this.id = id; 
        this.name = name; 
        this.area = area; 
        this.locality = locality; 
        this.latitude = latitude; 
        this.longitude = longitude; 
    } 
 
    // Getters and setters 
    public int getId() { return id; } 
    public void setId(int id) { this.id = id; } 
 
    public String getName() { return name; } 
    public void setName(String name) { this.name = name; } 
 
    public String getArea() { return area; } 
    public void setArea(String area) { this.area = area; } 
 
    public String getLocality() { return locality; } 
    public void setLocality(String locality) { this.locality = locality; } 
 
    public double getLatitude() { return latitude; } 
    public void setLatitude(double latitude) { this.latitude = latitude; } 
 
    public double getLongitude() { return longitude; } 
    public void setLongitude(double longitude) { this.longitude = longitude; 
} 
} 