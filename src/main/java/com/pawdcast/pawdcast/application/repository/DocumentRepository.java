package com.pawdcast.pawdcast.application.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pawdcast.pawdcast.application.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    
    // Find all documents by user ID, ordered by upload date (newest first)
    List<Document> findByUserIdOrderByUploadDateDesc(Integer userId);
    
    // Search documents by user ID and document name (case insensitive)
    List<Document> findByUserIdAndDocumentNameContainingIgnoreCase(Integer userId, String documentName);
    
    // Find documents by user ID and document type
    List<Document> findByUserIdAndDocumentType(Integer userId, String documentType);
    
    // Check if document exists and belongs to user
    boolean existsByDocumentIdAndUserId(Integer documentId, Integer userId);
    
    // Delete document by ID and user ID (for security)
    void deleteByDocumentIdAndUserId(Integer documentId, Integer userId);
}