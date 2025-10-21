package com.pawdcast.pawdcast.application.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class PetLegalHubService {

    public String getPetLegalHubJson() throws IOException {
        ClassPathResource resource = new ClassPathResource("petLegalHub.json");
        
        // Use InputStream instead of Files.readAllBytes for JAR compatibility
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
    
    // Optional: Add validation method
    public boolean validateJsonStructure(String json) {
        try {
            // Basic validation - you can add more specific validation
            return json != null && 
                   !json.trim().isEmpty() && 
                   json.contains("petLegalHubIndia") &&
                   json.contains("sections");
        } catch (Exception e) {
            return false;
        }
    }
    
    // Optional: Get basic info without loading entire JSON
    public String getLastUpdated() throws IOException {
        String json = getPetLegalHubJson();
        // Simple extraction - for production, use Jackson ObjectMapper
        if (json.contains("\"lastUpdated\":")) {
            int start = json.indexOf("\"lastUpdated\":\"") + 15;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        }
        return "Unknown";
    }
}