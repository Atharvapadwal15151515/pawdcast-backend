package com.pawdcast.pawdcast.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pet_profiles")
public class PetProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer petId;
    
    @Column(name = "owner_id")
    private Integer ownerId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "breed")
    private String breed;
    
    @Column(name = "dob")
    private LocalDate dob;
    
    @Column(name = "medical_records", columnDefinition = "TEXT")
    private String medicalRecords;
    
    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    @JsonIgnore  // Prevent circular reference and serialization issues
    private List<PetHealth> healthRecords;
    
    // Constructors
    public PetProfile() {
        this.createdAt = LocalDateTime.now();
    }
    
    public PetProfile(Integer ownerId, String name, String type, String breed, 
               LocalDate dob, String medicalRecords, byte[] photo) {
        this.ownerId = ownerId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.dob = dob;
        this.medicalRecords = medicalRecords;
        this.photo = photo;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getPetId() {
        return petId;
    }
    
    public void setPetId(Integer petId) {
        this.petId = petId;
    }
    
    public Integer getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getBreed() {
        return breed;
    }
    
    public void setBreed(String breed) {
        this.breed = breed;
    }
    
    public LocalDate getDob() {
        return dob;
    }
    
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    
    public String getMedicalRecords() {
        return medicalRecords;
    }
    
    public void setMedicalRecords(String medicalRecords) {
        this.medicalRecords = medicalRecords;
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
    
    public List<PetHealth> getHealthRecords() {
        return healthRecords;
    }
    
    public void setHealthRecords(List<PetHealth> healthRecords) {
        this.healthRecords = healthRecords;
    }
}