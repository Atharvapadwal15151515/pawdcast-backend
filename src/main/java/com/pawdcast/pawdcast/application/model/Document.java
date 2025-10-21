package com.pawdcast.pawdcast.application.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pet_digilocker")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "document_name", nullable = false, length = 255)
    private String documentName;

    @Column(name = "document_type", nullable = false, length = 255)
    private String documentType;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Lob
    @Column(name = "document_data", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] documentData;

    @Column(name = "description", length = 255)
    private String description;

    // Constructors
    public Document() {
        this.uploadDate = LocalDateTime.now();
    }

    public Document(Integer userId, String documentName, String documentType, 
                   byte[] documentData, String description) {
        this.userId = userId;
        this.documentName = documentName;
        this.documentType = documentType;
        this.documentData = documentData;
        this.description = description;
        this.uploadDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}