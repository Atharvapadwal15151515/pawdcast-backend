package com.pawdcast.pawdcast.application.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pawdcast.pawdcast.application.model.AdoptionSeeker;
import com.pawdcast.pawdcast.application.model.User;
import com.pawdcast.pawdcast.application.service.AdoptionSeekerService;
import com.pawdcast.pawdcast.application.service.AuthService;
import com.pawdcast.pawdcast.application.service.CertificateService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/seekers")
@CrossOrigin
public class AdoptionSeekerController {

    @Autowired
    private AdoptionSeekerService adoptionSeekerService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private AuthService authService;

    // Create new seeker with file uploads and return certificate - SECURED
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addSeeker(
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile idProof,
            @RequestParam(required = false) MultipartFile incomeProof,
            @RequestParam(required = false) MultipartFile residenceProof,
            @RequestParam(required = false) MultipartFile medicalRecords,
            @RequestParam(required = false) MultipartFile vaccinationCertificate,
            @RequestParam(required = false) MultipartFile adoptionAgreement,
            @RequestParam(required = false) MultipartFile photo,
            HttpServletRequest request
    ) throws IOException {
        
        // Get user from JWT token
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("User not logged in. Please login first.");
        }

        // Fetch user details from database
        User user = authService.findByEmail(userEmail);

        AdoptionSeeker seeker = new AdoptionSeeker();
        seeker.setAddress(address);
        seeker.setSeekerId(user.getId());

        // Track submitted documents
        List<String> submittedDocuments = new java.util.ArrayList<>();
        
        if (idProof != null && !idProof.isEmpty()) {
            seeker.setIdProof(idProof.getBytes());
            submittedDocuments.add("ID Proof");
        }
        if (incomeProof != null && !incomeProof.isEmpty()) {
            seeker.setIncomeProof(incomeProof.getBytes());
            submittedDocuments.add("Income Proof");
        }
        if (residenceProof != null && !residenceProof.isEmpty()) {
            seeker.setResidenceProof(residenceProof.getBytes());
            submittedDocuments.add("Residence Proof");
        }
        if (medicalRecords != null && !medicalRecords.isEmpty()) {
            seeker.setMedicalRecords(medicalRecords.getBytes());
            submittedDocuments.add("Medical Records");
        }
        if (vaccinationCertificate != null && !vaccinationCertificate.isEmpty()) {
            seeker.setVaccinationCertificate(vaccinationCertificate.getBytes());
            submittedDocuments.add("Vaccination Certificate");
        }
        if (adoptionAgreement != null && !adoptionAgreement.isEmpty()) {
            seeker.setAdoptionAgreement(adoptionAgreement.getBytes());
            submittedDocuments.add("Adoption Agreement");
        }
        if (photo != null && !photo.isEmpty()) {
            seeker.setPhoto(photo.getBytes());
        }

        AdoptionSeeker savedSeeker = adoptionSeekerService.saveSeeker(seeker);

        // Generate certificate PDF
        byte[] certificatePdf = certificateService.generateAdoptionCertificate(
            user.getFullName(),
            user.getEmail(),
            user.getPhone(),
            address,
            submittedDocuments,
            savedSeeker.getSeekerId(),
            photo != null && !photo.isEmpty() ? photo.getBytes() : null
        );

        // Return PDF as downloadable file
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"adoption-application-receipt-" + savedSeeker.getSeekerId() + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(certificatePdf);
    }

    // Get all seekers - SECURED
    @GetMapping("/all")
    public ResponseEntity<?> getAllSeekers(HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        return ResponseEntity.ok(adoptionSeekerService.getAllSeekers());
    }

    // Get seeker by ID - SECURED
    @GetMapping("/{id}")
    public ResponseEntity<?> getSeekerById(@PathVariable int id, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        Optional<AdoptionSeeker> seeker = adoptionSeekerService.getSeekerById(id);
        if (seeker.isPresent()) {
            return ResponseEntity.ok(seeker.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update seeker - SECURED
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateSeeker(
            @RequestParam Integer seekerId,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) MultipartFile idProof,
            @RequestParam(required = false) MultipartFile incomeProof,
            @RequestParam(required = false) MultipartFile residenceProof,
            @RequestParam(required = false) MultipartFile medicalRecords,
            @RequestParam(required = false) MultipartFile vaccinationCertificate,
            @RequestParam(required = false) MultipartFile adoptionAgreement,
            @RequestParam(required = false) MultipartFile photo,
            HttpServletRequest request
    ) throws IOException {
        
        // Get user from JWT token
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("User not logged in.");
        }

        // Fetch user details from database
        User user = authService.findByEmail(userEmail);
        Optional<AdoptionSeeker> existingSeeker = adoptionSeekerService.getSeekerById(seekerId);

        if (existingSeeker.isPresent()) {
            AdoptionSeeker seeker = existingSeeker.get();
            
            // Check permission - user can only update their own seeker record
            if (seeker.getSeekerId() != user.getId()) {
                return ResponseEntity.status(403).body("You don't have permission to update this seeker record.");
            }

            if (address != null) seeker.setAddress(address);
            if (idProof != null && !idProof.isEmpty()) seeker.setIdProof(idProof.getBytes());
            if (incomeProof != null && !incomeProof.isEmpty()) seeker.setIncomeProof(incomeProof.getBytes());
            if (residenceProof != null && !residenceProof.isEmpty()) seeker.setResidenceProof(residenceProof.getBytes());
            if (medicalRecords != null && !medicalRecords.isEmpty()) seeker.setMedicalRecords(medicalRecords.getBytes());
            if (vaccinationCertificate != null && !vaccinationCertificate.isEmpty()) seeker.setVaccinationCertificate(vaccinationCertificate.getBytes());
            if (adoptionAgreement != null && !adoptionAgreement.isEmpty()) seeker.setAdoptionAgreement(adoptionAgreement.getBytes());
            if (photo != null && !photo.isEmpty()) seeker.setPhoto(photo.getBytes());

            return ResponseEntity.ok(adoptionSeekerService.updateSeeker(seeker));
        }
        return ResponseEntity.notFound().build();
    }

    // Delete seeker - SECURED
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSeeker(@PathVariable int id, HttpServletRequest request) {
        // Get user from JWT token
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("User not logged in.");
        }
        
        // Fetch user details from database
        User user = authService.findByEmail(userEmail);
        Optional<AdoptionSeeker> seeker = adoptionSeekerService.getSeekerById(id);
        
        if (seeker.isPresent()) {
            // Check permission - user can only delete their own seeker record
            if (seeker.get().getSeekerId() != user.getId()) {
                return ResponseEntity.status(403).body("You don't have permission to delete this seeker record.");
            }
            
            adoptionSeekerService.deleteSeeker(id);
            return ResponseEntity.ok().body("Seeker record deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Check if seeker exists - SECURED
    @GetMapping("/exists/{id}")
    public ResponseEntity<?> seekerExists(@PathVariable int id, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        return ResponseEntity.ok(adoptionSeekerService.seekerExists(id));
    }

    // Get seeker photo - SECURED
    @GetMapping("/{id}/photo")
    public ResponseEntity<?> getSeekerPhoto(@PathVariable int id, HttpServletRequest request) {
        // Check authentication
        String userEmail = (String) request.getAttribute("userEmail");
        if (userEmail == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        Optional<AdoptionSeeker> seeker = adoptionSeekerService.getSeekerById(id);
        if (seeker.isPresent() && seeker.get().getPhoto() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(seeker.get().getPhoto());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}