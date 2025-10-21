package com.pawdcast.pawdcast.application.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Controller to serve Pet Insurance information and estimator data.
 * 
 * Two endpoints:
 * 1. /api/insurance/info - Returns insurance-info.json
 * 2. /api/insurance/estimator - Returns insurance-estimator.json
 */
@RestController
@RequestMapping("/api/insurance")
public class PetInsuranceController {

    /**
     * Reads the insurance-info.json file from resources and returns its content.
     *
     * @return ResponseEntity with JSON content or error message.
     */
    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getInsuranceInfo() {
        try {
            ClassPathResource resource = new ClassPathResource("insurance-info.json");

            // Read the JSON file content
            String json = new String(Files.readAllBytes(resource.getFile().toPath()));

            // Return JSON response
            return ResponseEntity.ok(json);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body("{\"error\":\"Could not load insurance info\"}");
        }
    }

    /**
     * Reads the insurance-estimator.json file from resources and returns its content.
     *
     * @return ResponseEntity with JSON content or error message.
     */
    @GetMapping(value = "/estimator", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getInsuranceEstimator() {
        try {
            ClassPathResource resource = new ClassPathResource("insurance-estimator.json");

            // Read the JSON file content
            String json = new String(Files.readAllBytes(resource.getFile().toPath()));

            // Return JSON response
            return ResponseEntity.ok(json);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(500)
                    .body("{\"error\":\"Could not load insurance estimator data\"}");
        }
    }
}
