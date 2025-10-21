package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "adoption_seekers")
public class AdoptionSeeker {

    @Id
    @Column(name = "seeker_id")
    private int seekerId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Lob
    @Column(name = "id_proof", columnDefinition = "LONGBLOB")
    private byte[] idProof;

    @Lob
    @Column(name = "income_proof", columnDefinition = "LONGBLOB")
    private byte[] incomeProof;

    @Lob
    @Column(name = "residence_proof", columnDefinition = "LONGBLOB")
    private byte[] residenceProof;

    @Lob
    @Column(name = "medical_records", columnDefinition = "LONGBLOB")
    private byte[] medicalRecords;

    @Lob
    @Column(name = "vaccination_certificate", columnDefinition = "LONGBLOB")
    private byte[] vaccinationCertificate;

    @Lob
    @Column(name = "adoption_agreement", columnDefinition = "LONGBLOB")
    private byte[] adoptionAgreement;

    @Column(length = 50)
    private String status = "pending";

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    // -------------------- Constructors --------------------
    public AdoptionSeeker() {}

    public AdoptionSeeker(int seekerId, String address, byte[] idProof, byte[] incomeProof,
                           byte[] residenceProof, byte[] medicalRecords,
                           byte[] vaccinationCertificate, byte[] adoptionAgreement,
                           String status, byte[] photo) {
        this.seekerId = seekerId;
        this.address = address;
        this.idProof = idProof;
        this.incomeProof = incomeProof;
        this.residenceProof = residenceProof;
        this.medicalRecords = medicalRecords;
        this.vaccinationCertificate = vaccinationCertificate;
        this.adoptionAgreement = adoptionAgreement;
        this.status = status;
        this.photo = photo;
    }

    // -------------------- Getters & Setters --------------------
    public int getSeekerId() { return seekerId; }
    public void setSeekerId(int seekerId) { this.seekerId = seekerId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public byte[] getIdProof() { return idProof; }
    public void setIdProof(byte[] idProof) { this.idProof = idProof; }

    public byte[] getIncomeProof() { return incomeProof; }
    public void setIncomeProof(byte[] incomeProof) { this.incomeProof = incomeProof; }

    public byte[] getResidenceProof() { return residenceProof; }
    public void setResidenceProof(byte[] residenceProof) { this.residenceProof = residenceProof; }

    public byte[] getMedicalRecords() { return medicalRecords; }
    public void setMedicalRecords(byte[] medicalRecords) { this.medicalRecords = medicalRecords; }

    public byte[] getVaccinationCertificate() { return vaccinationCertificate; }
    public void setVaccinationCertificate(byte[] vaccinationCertificate) { this.vaccinationCertificate = vaccinationCertificate; }

    public byte[] getAdoptionAgreement() { return adoptionAgreement; }
    public void setAdoptionAgreement(byte[] adoptionAgreement) { this.adoptionAgreement = adoptionAgreement; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
