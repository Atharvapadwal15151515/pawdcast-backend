package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "adoptions")
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adoption_id")
    private int adoptionId;

    @Column(name = "seeker_id")
    private Integer seekerId;

    @Column(name = "giver_id")
    private Integer giverId;

    @Column(name = "adoption_status", length = 50)
    private String adoptionStatus = "pending";

    @Column(name = "application_date", insertable = false, updatable = false)
    private Timestamp applicationDate;

    @Column(name = "approval_date")
    private Timestamp approvalDate;

    @Column(name = "completion_date")
    private Timestamp completionDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Lob
    @Column(name = "adoption_agreement", columnDefinition = "LONGBLOB")
    private byte[] adoptionAgreement;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    // -------------------- Constructors --------------------
    public Adoption() {}

    public Adoption(Integer seekerId, Integer giverId, String adoptionStatus,
                    String notes, byte[] adoptionAgreement) {
        this.seekerId = seekerId;
        this.giverId = giverId;
        this.adoptionStatus = adoptionStatus;
        this.notes = notes;
        this.adoptionAgreement = adoptionAgreement;
    }

    // -------------------- Getters & Setters --------------------
    public int getAdoptionId() { return adoptionId; }
    public void setAdoptionId(int adoptionId) { this.adoptionId = adoptionId; }

    public Integer getSeekerId() { return seekerId; }
    public void setSeekerId(Integer seekerId) { this.seekerId = seekerId; }

    public Integer getGiverId() { return giverId; }
    public void setGiverId(Integer giverId) { this.giverId = giverId; }

    public String getAdoptionStatus() { return adoptionStatus; }
    public void setAdoptionStatus(String adoptionStatus) { this.adoptionStatus = adoptionStatus; }

    public Timestamp getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Timestamp applicationDate) { this.applicationDate = applicationDate; }

    public Timestamp getApprovalDate() { return approvalDate; }
    public void setApprovalDate(Timestamp approvalDate) { this.approvalDate = approvalDate; }

    public Timestamp getCompletionDate() { return completionDate; }
    public void setCompletionDate(Timestamp completionDate) { this.completionDate = completionDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public byte[] getAdoptionAgreement() { return adoptionAgreement; }
    public void setAdoptionAgreement(byte[] adoptionAgreement) { this.adoptionAgreement = adoptionAgreement; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
