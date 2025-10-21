package com.pawdcast.pawdcast.application.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pawdcast.pawdcast.application.model.PetGiver;
import com.pawdcast.pawdcast.application.service.PetGiverService;

import java.io.IOException;

@RestController
@RequestMapping("/pets")
@CrossOrigin
public class PetGiverController {

    @Autowired
    private PetGiverService petGiverService;

    // Create new pet with file uploads
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PetGiver addPet(
            @RequestParam String petName,
            @RequestParam String petType,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String healthStatus,
            @RequestParam(required = false) MultipartFile petPhoto,
            @RequestParam(required = false) MultipartFile medicalRecords,
            @RequestParam(required = false) MultipartFile vaccinationCertificate,
            @RequestParam(required = false) MultipartFile ownershipProof,
            @RequestParam(required = false) MultipartFile residenceProof,
            @RequestParam(required = false) MultipartFile surrenderAgreement
    ) throws IOException {
        PetGiver pet = new PetGiver();
        pet.setPetName(petName);
        pet.setPetType(petType);
        pet.setBreed(breed);
        pet.setAge(age);
        pet.setHealthStatus(healthStatus);
        pet.setAdoptionStatus("available");

        if (petPhoto != null && !petPhoto.isEmpty()) pet.setPetPhoto(petPhoto.getBytes());
        if (medicalRecords != null && !medicalRecords.isEmpty()) pet.setMedicalRecords(medicalRecords.getBytes());
        if (vaccinationCertificate != null && !vaccinationCertificate.isEmpty()) pet.setVaccinationCertificate(vaccinationCertificate.getBytes());
        if (ownershipProof != null && !ownershipProof.isEmpty()) pet.setOwnershipProof(ownershipProof.getBytes());
        if (residenceProof != null && !residenceProof.isEmpty()) pet.setResidenceProof(residenceProof.getBytes());
        if (surrenderAgreement != null && !surrenderAgreement.isEmpty()) pet.setSurrenderAgreement(surrenderAgreement.getBytes());

        return petGiverService.savePetGiver(pet);
    }

    // Get all pets
    @GetMapping("/all")
    public List<PetGiver> getAllPets() {
        return petGiverService.getAllPets();
    }

    // Get available pets
    @GetMapping("/available")
    public List<PetGiver> getAvailablePets() {
        return petGiverService.getAvailablePets();
    }

    // Get pet by ID
    @GetMapping("/{id}")
    public Optional<PetGiver> getPetById(@PathVariable int id) {
        return petGiverService.getPetById(id);
    }

    // Serve pet photo
    @GetMapping("/{id}/photo")
    public ResponseEntity<byte[]> getPetPhoto(@PathVariable int id) {
        return serveFile(id, "photo");
    }

    // Serve medical records as image
    @GetMapping("/{id}/medical-records")
    public ResponseEntity<byte[]> getMedicalRecords(@PathVariable int id) {
        return serveFile(id, "medicalRecords");
    }

    // Serve vaccination certificate as image
    @GetMapping("/{id}/vaccination-certificate")
    public ResponseEntity<byte[]> getVaccinationCertificate(@PathVariable int id) {
        return serveFile(id, "vaccinationCertificate");
    }

    // Serve ownership proof as image
    @GetMapping("/{id}/ownership-proof")
    public ResponseEntity<byte[]> getOwnershipProof(@PathVariable int id) {
        return serveFile(id, "ownershipProof");
    }

    // Serve residence proof as image
    @GetMapping("/{id}/residence-proof")
    public ResponseEntity<byte[]> getResidenceProof(@PathVariable int id) {
        return serveFile(id, "residenceProof");
    }

    // Serve surrender agreement as image
    @GetMapping("/{id}/surrender-agreement")
    public ResponseEntity<byte[]> getSurrenderAgreement(@PathVariable int id) {
        return serveFile(id, "surrenderAgreement");
    }

    // Generic method to serve files
    private ResponseEntity<byte[]> serveFile(int id, String fileType) {
        Optional<PetGiver> pet = petGiverService.getPetById(id);
        if (pet.isPresent()) {
            byte[] fileData = null;

            switch (fileType) {
                case "photo":
                    fileData = pet.get().getPetPhoto();
                    break;
                case "medicalRecords":
                    fileData = pet.get().getMedicalRecords();
                    break;
                case "vaccinationCertificate":
                    fileData = pet.get().getVaccinationCertificate();
                    break;
                case "ownershipProof":
                    fileData = pet.get().getOwnershipProof();
                    break;
                case "residenceProof":
                    fileData = pet.get().getResidenceProof();
                    break;
                case "surrenderAgreement":
                    fileData = pet.get().getSurrenderAgreement();
                    break;
            }

            if (fileData != null) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                        .body(fileData);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
