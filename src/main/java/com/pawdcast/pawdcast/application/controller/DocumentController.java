package com.pawdcast.pawdcast.application.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pawdcast.pawdcast.application.model.Document;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.DocumentService;
import com.pawdcast.pawdcast.application.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/digilocker")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private AuthService authService;

    private Integer getUserIdFromRequest(HttpServletRequest request) {
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            throw new RuntimeException("User not logged in");
        }
        User user = authService.findByEmail(userEmail);
        return user.getId();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            HttpServletRequest request,
            @RequestParam String documentName,
            @RequestParam String documentType,
            @RequestParam(required = false) String description,
            @RequestParam MultipartFile file) {
        
        try {
            Integer userId = getUserIdFromRequest(request);

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File cannot be empty");
            }

            // Store file in database
            Document document = documentService.uploadDocument(
                userId, documentName, documentType, description, file
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Image uploaded successfully");
            response.put("documentId", document.getDocumentId());
            response.put("documentName", document.getDocumentName());
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error uploading image: " + e.getMessage());
        }
    }

    @GetMapping("/documents")
    public ResponseEntity<?> getUserDocuments(HttpServletRequest request) {
        try {
            Integer userId = getUserIdFromRequest(request);
            List<Document> documents = documentService.getUserDocuments(userId);
            
            // Return document list without file data
            List<Map<String, Object>> responseList = new java.util.ArrayList<>();
            for (Document doc : documents) {
                Map<String, Object> docInfo = new HashMap<>();
                docInfo.put("documentId", doc.getDocumentId());
                docInfo.put("documentName", doc.getDocumentName());
                docInfo.put("documentType", doc.getDocumentType());
                docInfo.put("uploadDate", doc.getUploadDate());
                docInfo.put("description", doc.getDescription());
                docInfo.put("fileSize", doc.getDocumentData() != null ? doc.getDocumentData().length : 0);
                responseList.add(docInfo);
            }
            
            return ResponseEntity.ok(responseList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/document/{documentId}/view")
    public ResponseEntity<?> viewDocument(
            @PathVariable Integer documentId,
            HttpServletRequest request) {
        
        try {
            Integer userId = getUserIdFromRequest(request);
            Document document = documentService.getDocument(documentId, userId);
            
            if (document.getDocumentData() == null || document.getDocumentData().length == 0) {
                return ResponseEntity.status(404).body("Image data not found");
            }

            // Convert binary data to Base64 for frontend
            String base64Data = Base64.getEncoder().encodeToString(document.getDocumentData());
            
            Map<String, Object> response = new HashMap<>();
            response.put("documentId", document.getDocumentId());
            response.put("documentName", document.getDocumentName());
            response.put("documentType", document.getDocumentType());
            response.put("uploadDate", document.getUploadDate());
            response.put("description", document.getDescription());
            response.put("fileSize", document.getDocumentData().length);
            response.put("documentData", base64Data);
            response.put("contentType", getImageContentType(document.getDocumentName()));
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).body("Image not found");
            }
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/document/{documentId}/download")
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Integer documentId,
            HttpServletRequest request) {
        
        try {
            Integer userId = getUserIdFromRequest(request);
            Document document = documentService.getDocument(documentId, userId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(getImageContentType(document.getDocumentName())));
            headers.setContentDispositionFormData("attachment", document.getDocumentName());
            headers.setContentLength(document.getDocumentData().length);
            
            return new ResponseEntity<>(document.getDocumentData(), headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).build();
        }
    }

    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable Integer documentId, HttpServletRequest request) {
        try {
            Integer userId = getUserIdFromRequest(request);
            documentService.deleteDocument(documentId, userId);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchDocuments(
            HttpServletRequest request,
            @RequestParam String query) {
        try {
            Integer userId = getUserIdFromRequest(request);
            List<Document> documents = documentService.searchDocuments(userId, query);
            return ResponseEntity.ok(createDocumentListResponse(documents));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @GetMapping("/type/{documentType}")
    public ResponseEntity<?> getDocumentsByType(
            @PathVariable String documentType,
            HttpServletRequest request) {
        try {
            Integer userId = getUserIdFromRequest(request);
            List<Document> documents = documentService.getDocumentsByType(userId, documentType);
            return ResponseEntity.ok(createDocumentListResponse(documents));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    private List<Map<String, Object>> createDocumentListResponse(List<Document> documents) {
        List<Map<String, Object>> responseList = new java.util.ArrayList<>();
        for (Document doc : documents) {
            Map<String, Object> docInfo = new HashMap<>();
            docInfo.put("documentId", doc.getDocumentId());
            docInfo.put("documentName", doc.getDocumentName());
            docInfo.put("documentType", doc.getDocumentType());
            docInfo.put("uploadDate", doc.getUploadDate());
            docInfo.put("description", doc.getDescription());
            docInfo.put("fileSize", doc.getDocumentData() != null ? doc.getDocumentData().length : 0);
            responseList.add(docInfo);
        }
        return responseList;
    }

    private String getImageContentType(String fileName) {
        if (fileName == null) return "image/jpeg";
        
        String lowerFileName = fileName.toLowerCase();
        
        if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) return "image/jpeg";
        if (lowerFileName.endsWith(".png")) return "image/png";
        if (lowerFileName.endsWith(".gif")) return "image/gif";
        if (lowerFileName.endsWith(".bmp")) return "image/bmp";
        if (lowerFileName.endsWith(".webp")) return "image/webp";
        if (lowerFileName.endsWith(".svg")) return "image/svg+xml";
        
        // Default to JPEG if unknown
        return "image/jpeg";
    }
}