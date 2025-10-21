package com.pawdcast.pawdcast.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "pet_health")
public class PetHealth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_id")
    private Long healthId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @JsonIgnore  // Add this to prevent serialization issues
    private PetProfile pet;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "height")
    private Double height;

    @Column(name = "diet_notes")
    private String dietNotes;

    @Column(name = "medical_conditions", columnDefinition = "TEXT")
    private String medicalConditions;

    @Column(name = "vaccination_records", columnDefinition = "TEXT")
    private String vaccinationRecords;

    @Column(name = "last_vet_visit")
    private LocalDate lastVetVisit;

    @Column(name = "next_vet_visit")
    private LocalDate nextVetVisit;

    @Column(name = "medications", columnDefinition = "TEXT")
    private String medications;

    @Column(name = "activity_level")
    private String activityLevel;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = true)
    private Timestamp updatedAt;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "exercise_notes")
    private String exerciseNotes;

    @Column(name = "medical_notes")
    private String medicalNotes;

    @Column(name = "next_vet_date")
    private LocalDate nextVetDate;

    // Getters & Setters
    public Long getHealthId() { return healthId; }
    public void setHealthId(Long healthId) { this.healthId = healthId; }

    public PetProfile getPet() { return pet; }
    public void setPet(PetProfile pet) { this.pet = pet; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }

    public String getDietNotes() { return dietNotes; }
    public void setDietNotes(String dietNotes) { this.dietNotes = dietNotes; }

    public String getMedicalConditions() { return medicalConditions; }
    public void setMedicalConditions(String medicalConditions) { this.medicalConditions = medicalConditions; }

    public String getVaccinationRecords() { return vaccinationRecords; }
    public void setVaccinationRecords(String vaccinationRecords) { this.vaccinationRecords = vaccinationRecords; }

    public LocalDate getLastVetVisit() { return lastVetVisit; }
    public void setLastVetVisit(LocalDate lastVetVisit) { this.lastVetVisit = lastVetVisit; }

    public LocalDate getNextVetVisit() { return nextVetVisit; }
    public void setNextVetVisit(LocalDate nextVetVisit) { this.nextVetVisit = nextVetVisit; }

    public String getMedications() { return medications; }
    public void setMedications(String medications) { this.medications = medications; }

    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public String getExerciseNotes() { return exerciseNotes; }
    public void setExerciseNotes(String exerciseNotes) { this.exerciseNotes = exerciseNotes; }

    public String getMedicalNotes() { return medicalNotes; }
    public void setMedicalNotes(String medicalNotes) { this.medicalNotes = medicalNotes; }

    public LocalDate getNextVetDate() { return nextVetDate; }
    public void setNextVetDate(LocalDate nextVetDate) { this.nextVetDate = nextVetDate; }
}