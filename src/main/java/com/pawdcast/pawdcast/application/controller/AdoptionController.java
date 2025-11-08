package com.pawdcast.pawdcast.application.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pawdcast.pawdcast.application.model.Adoption;
import com.pawdcast.pawdcast.application.service.AdoptionService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/adoptions")
@CrossOrigin
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    // Create adoption request with multipart/form-data support - SECURED
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addAdoption(
            @RequestParam Integer seekerId,
            @RequestParam Integer giverId,
            @RequestParam(required = false) String adoptionStatus,
            @RequestParam(required = false) MultipartFile adoptionAgreement,
            @RequestParam(required = false) String notes,
            HttpServletRequest request
    ) throws IOException {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        Adoption adoption = new Adoption();
        adoption.setSeekerId(seekerId);
        adoption.setGiverId(giverId);
        adoption.setAdoptionStatus(adoptionStatus != null ? adoptionStatus : "pending");
        adoption.setNotes(notes);

        if (adoptionAgreement != null && !adoptionAgreement.isEmpty()) {
            adoption.setAdoptionAgreement(adoptionAgreement.getBytes());
        }

        return ResponseEntity.ok(adoptionService.saveAdoption(adoption));
    }

    // Get all adoptions - SECURED
    @GetMapping("/all")
    public ResponseEntity<?> getAllAdoptions(HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        return ResponseEntity.ok(adoptionService.getAllAdoptions());
    }

    // Get adoption by ID - SECURED
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdoptionById(@PathVariable int id, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        Optional<Adoption> adoption = adoptionService.getAdoptionById(id);
        if (adoption.isPresent()) {
            return ResponseEntity.ok(adoption.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get adoptions by seeker - SECURED
    @GetMapping("/seeker/{seekerId}")
    public ResponseEntity<?> getAdoptionsBySeeker(@PathVariable int seekerId, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        return ResponseEntity.ok(adoptionService.getAdoptionsBySeekerId(seekerId));
    }

    // Get adoptions by giver - SECURED
    @GetMapping("/giver/{giverId}")
    public ResponseEntity<?> getAdoptionsByGiver(@PathVariable int giverId, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        return ResponseEntity.ok(adoptionService.getAdoptionsByGiverId(giverId));
    }

    // Update adoption - SECURED
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAdoption(
            @RequestParam Integer adoptionId,
            @RequestParam(required = false) Integer seekerId,
            @RequestParam(required = false) Integer giverId,
            @RequestParam(required = false) String adoptionStatus,
            @RequestParam(required = false) MultipartFile adoptionAgreement,
            @RequestParam(required = false) String notes,
            HttpServletRequest request
    ) throws IOException {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        Optional<Adoption> existingAdoption = adoptionService.getAdoptionById(adoptionId);
        if (existingAdoption.isPresent()) {
            Adoption adoption = existingAdoption.get();

            if (seekerId != null) adoption.setSeekerId(seekerId);
            if (giverId != null) adoption.setGiverId(giverId);
            if (adoptionStatus != null) adoption.setAdoptionStatus(adoptionStatus);
            if (notes != null) adoption.setNotes(notes);
            if (adoptionAgreement != null && !adoptionAgreement.isEmpty()) {
                adoption.setAdoptionAgreement(adoptionAgreement.getBytes());
            }

            return ResponseEntity.ok(adoptionService.updateAdoption(adoption));
        }
        return ResponseEntity.notFound().build();
    }

    // Delete adoption - SECURED
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAdoption(@PathVariable int id, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        adoptionService.deleteAdoption(id);
        return ResponseEntity.ok().body("Adoption deleted successfully");
    }

    // Serve adoption agreement document - SECURED
    @GetMapping("/{id}/adoption-agreement")
    public ResponseEntity<?> getAdoptionAgreement(@PathVariable int id, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        Optional<Adoption> adoption = adoptionService.getAdoptionById(id);
        if (adoption.isPresent() && adoption.get().getAdoptionAgreement() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(adoption.get().getAdoptionAgreement());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}