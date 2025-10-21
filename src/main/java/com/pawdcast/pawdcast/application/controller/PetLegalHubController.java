package com.pawdcast.pawdcast.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pawdcast.pawdcast.application.service.PetLegalHubService;

import java.io.IOException;

@RestController
@RequestMapping("/api/legal")
public class PetLegalHubController {

    @Autowired
    private PetLegalHubService petLegalHubService;

    @GetMapping(value = "/pet-legal-hub", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPetLegalHub() {
        try {
            String jsonData = petLegalHubService.getPetLegalHubJson();
            
            // Basic validation
            if (!petLegalHubService.validateJsonStructure(jsonData)) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"error\": \"Invalid JSON structure\"}");
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonData);
                    
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Unable to load pet legal hub data\", \"message\": \"JSON file not found or inaccessible\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Unexpected error occurred\", \"message\": \"" + e.getMessage() + "\"}");
        }
    }
    
    // Health check endpoint
    @GetMapping("/pet-legal-hub/health")
    public ResponseEntity<String> healthCheck() {
        try {
            String lastUpdated = petLegalHubService.getLastUpdated();
            return ResponseEntity.ok("{\"status\": \"healthy\", \"lastUpdated\": \"" + lastUpdated + "\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"status\": \"unhealthy\", \"error\": \"Service unavailable\"}");
        }
    }
}