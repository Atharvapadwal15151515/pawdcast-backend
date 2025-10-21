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
import com.pawdcast.pawdcast.application.service.CertificateService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/seekers")
@CrossOrigin
public class AdoptionSeekerController {

    @Autowired
    private AdoptionSeekerService adoptionSeekerService;

    @Autowired
    private CertificateService certificateService;

    // Create new seeker with file uploads and return certificate
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> addSeeker(
            @RequestParam String address,
            @RequestParam(required = false) MultipartFile idProof,
            @RequestParam(required = false) MultipartFile incomeProof,
            @RequestParam(required = false) MultipartFile residenceProof,
            @RequestParam(required = false) MultipartFile medicalRecords,
            @RequestParam(required = false) MultipartFile vaccinationCertificate,
            @RequestParam(required = false) MultipartFile adoptionAgreement,
            @RequestParam(required = false) MultipartFile photo,
            HttpSession session
    ) throws IOException {
        
        // Get user from session
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in. Please login first.");
        }

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

    // Other methods remain the same...
    @GetMapping("/all")
    public List<AdoptionSeeker> getAllSeekers() {
        return adoptionSeekerService.getAllSeekers();
    }

    @GetMapping("/{id}")
    public Optional<AdoptionSeeker> getSeekerById(@PathVariable int id) {
        return adoptionSeekerService.getSeekerById(id);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdoptionSeeker updateSeeker(
            @RequestParam Integer seekerId,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) MultipartFile idProof,
            @RequestParam(required = false) MultipartFile incomeProof,
            @RequestParam(required = false) MultipartFile residenceProof,
            @RequestParam(required = false) MultipartFile medicalRecords,
            @RequestParam(required = false) MultipartFile vaccinationCertificate,
            @RequestParam(required = false) MultipartFile adoptionAgreement,
            @RequestParam(required = false) MultipartFile photo,
            HttpSession session
    ) throws IOException {
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in.");
        }

        Optional<AdoptionSeeker> existingSeeker = adoptionSeekerService.getSeekerById(seekerId);

        if (existingSeeker.isPresent()) {
            AdoptionSeeker seeker = existingSeeker.get();
            
            if (seeker.getSeekerId() != user.getId()) {
                throw new RuntimeException("You don't have permission to update this seeker record.");
            }

            if (address != null) seeker.setAddress(address);
            if (idProof != null && !idProof.isEmpty()) seeker.setIdProof(idProof.getBytes());
            if (incomeProof != null && !incomeProof.isEmpty()) seeker.setIncomeProof(incomeProof.getBytes());
            if (residenceProof != null && !residenceProof.isEmpty()) seeker.setResidenceProof(residenceProof.getBytes());
            if (medicalRecords != null && !medicalRecords.isEmpty()) seeker.setMedicalRecords(medicalRecords.getBytes());
            if (vaccinationCertificate != null && !vaccinationCertificate.isEmpty()) seeker.setVaccinationCertificate(vaccinationCertificate.getBytes());
            if (adoptionAgreement != null && !adoptionAgreement.isEmpty()) seeker.setAdoptionAgreement(adoptionAgreement.getBytes());
            if (photo != null && !photo.isEmpty()) seeker.setPhoto(photo.getBytes());

            return adoptionSeekerService.updateSeeker(seeker);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSeeker(@PathVariable int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not logged in.");
        }
        
        Optional<AdoptionSeeker> seeker = adoptionSeekerService.getSeekerById(id);
        if (seeker.isPresent() && seeker.get().getSeekerId() != user.getId()) {
            throw new RuntimeException("You don't have permission to delete this seeker record.");
        }
        
        adoptionSeekerService.deleteSeeker(id);
    }

    @GetMapping("/exists/{id}")
    public boolean seekerExists(@PathVariable int id) {
        return adoptionSeekerService.seekerExists(id);
    }

    @GetMapping("/{id}/photo")
    public @ResponseBody byte[] getSeekerPhoto(@PathVariable int id) {
        Optional<AdoptionSeeker> seeker = adoptionSeekerService.getSeekerById(id);
        return seeker.map(AdoptionSeeker::getPhoto).orElse(null);
    }
}