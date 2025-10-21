package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "pet_giver")
public class PetGiver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giver_id")
    private int giverId;

    @Column(name = "pet_name", nullable = false, length = 100)
    private String petName;

    @Column(name = "pet_type", nullable = false, length = 50)
    private String petType;

    @Column(name = "breed", length = 100)
    private String breed;

    @Column(name = "age")
    private Integer age;

    @Column(name = "health_status", columnDefinition = "TEXT")
    private String healthStatus;

    // -------------------- LOB FIELDS --------------------
    @Lob
    @Column(name = "pet_photo", columnDefinition = "LONGBLOB")
    private byte[] petPhoto;

    @Lob
    @Column(name = "medical_records", columnDefinition = "LONGBLOB")
    private byte[] medicalRecords;

    @Lob
    @Column(name = "vaccination_certificate", columnDefinition = "LONGBLOB")
    private byte[] vaccinationCertificate;

    @Lob
    @Column(name = "ownership_proof", columnDefinition = "LONGBLOB")
    private byte[] ownershipProof;

    @Lob
    @Column(name = "residence_proof", columnDefinition = "LONGBLOB")
    private byte[] residenceProof;

    @Lob
    @Column(name = "surrender_agreement", columnDefinition = "LONGBLOB")
    private byte[] surrenderAgreement;

    // -----------------------------------------------------

    @Column(name = "adoption_status", length = 50)
    private String adoptionStatus = "available";

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    // -------------------- Constructors --------------------
    public PetGiver() {}

    public PetGiver(String petName, String petType, String breed, Integer age, String healthStatus,
                    byte[] petPhoto, byte[] medicalRecords, byte[] vaccinationCertificate,
                    byte[] ownershipProof, byte[] residenceProof, byte[] surrenderAgreement,
                    String adoptionStatus) {
        this.petName = petName;
        this.petType = petType;
        this.breed = breed;
        this.age = age;
        this.healthStatus = healthStatus;
        this.petPhoto = petPhoto;
        this.medicalRecords = medicalRecords;
        this.vaccinationCertificate = vaccinationCertificate;
        this.ownershipProof = ownershipProof;
        this.residenceProof = residenceProof;
        this.surrenderAgreement = surrenderAgreement;
        this.adoptionStatus = adoptionStatus;
    }

    // -------------------- Getters & Setters --------------------
    public int getGiverId() {
        return giverId;
    }

    public void setGiverId(int giverId) {
        this.giverId = giverId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public byte[] getPetPhoto() {
        return petPhoto;
    }

    public void setPetPhoto(byte[] petPhoto) {
        this.petPhoto = petPhoto;
    }

    public byte[] getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(byte[] medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public byte[] getVaccinationCertificate() {
        return vaccinationCertificate;
    }

    public void setVaccinationCertificate(byte[] vaccinationCertificate) {
        this.vaccinationCertificate = vaccinationCertificate;
    }

    public byte[] getOwnershipProof() {
        return ownershipProof;
    }

    public void setOwnershipProof(byte[] ownershipProof) {
        this.ownershipProof = ownershipProof;
    }

    public byte[] getResidenceProof() {
        return residenceProof;
    }

    public void setResidenceProof(byte[] residenceProof) {
        this.residenceProof = residenceProof;
    }

    public byte[] getSurrenderAgreement() {
        return surrenderAgreement;
    }

    public void setSurrenderAgreement(byte[] surrenderAgreement) {
        this.surrenderAgreement = surrenderAgreement;
    }

    public String getAdoptionStatus() {
        return adoptionStatus;
    }

    public void setAdoptionStatus(String adoptionStatus) {
        this.adoptionStatus = adoptionStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
