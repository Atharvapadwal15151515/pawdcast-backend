package com.pawdcast.pawdcast.application.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pawdcast.pawdcast.application.model.Document;
import com.pawdcast.pawdcast.application.repository.DocumentRepository;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    // Supported image types
    private static final String[] SUPPORTED_IMAGE_TYPES = {
        "image/jpeg", "image/jpg", "image/png", "image/gif", 
        "image/bmp", "image/webp", "image/svg+xml"
    };

    public Document uploadDocument(Integer userId, String documentName, String documentType, 
                                  String description, MultipartFile file) throws IOException {
        
        // Validate file type - only images allowed
        if (!isImageFile(file)) {
            throw new IllegalArgumentException("Only image files are allowed (JPEG, PNG, GIF, BMP, WEBP, SVG)");
        }

        // Validate file size (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size must be less than 10MB");
        }

        // Convert MultipartFile to byte array
        byte[] documentData = file.getBytes();
        
        // Create new document entity
        Document document = new Document();
        document.setUserId(userId);
        document.setDocumentName(documentName);
        document.setDocumentType(documentType);
        document.setDescription(description);
        document.setDocumentData(documentData);
        
        // Save to database
        return documentRepository.save(document);
    }

    public List<Document> getUserDocuments(Integer userId) {
        return documentRepository.findByUserIdOrderByUploadDateDesc(userId);
    }

    public Document getDocument(Integer documentId, Integer userId) {
        Optional<Document> document = documentRepository.findById(documentId);
        
        if (document.isEmpty()) {
            throw new RuntimeException("Document not found");
        }
        
        // Verify document belongs to user
        if (!document.get().getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }
        
        return document.get();
    }

    public void deleteDocument(Integer documentId, Integer userId) {
        // Verify document exists and belongs to user
        if (!documentRepository.existsByDocumentIdAndUserId(documentId, userId)) {
            throw new RuntimeException("Document not found or access denied");
        }
        
        // Delete from database
        documentRepository.deleteByDocumentIdAndUserId(documentId, userId);
    }

    // Search documents by name for a specific user
    public List<Document> searchDocuments(Integer userId, String query) {
        return documentRepository.findByUserIdAndDocumentNameContainingIgnoreCase(userId, query);
    }

    // Get documents by type for a specific user
    public List<Document> getDocumentsByType(Integer userId, String documentType) {
        return documentRepository.findByUserIdAndDocumentType(userId, documentType);
    }

    // Helper method to check if file is an image
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        
        for (String supportedType : SUPPORTED_IMAGE_TYPES) {
            if (supportedType.equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }
}