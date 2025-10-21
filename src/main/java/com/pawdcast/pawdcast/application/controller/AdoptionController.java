package com.pawdcast.pawdcast.application.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.pawdcast.pawdcast.application.model.Adoption;
import com.pawdcast.pawdcast.application.service.AdoptionService;

@RestController
@RequestMapping("/adoptions")
@CrossOrigin
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    // Create adoption request with multipart/form-data support
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Adoption addAdoption(
            @RequestParam Integer seekerId,
            @RequestParam Integer giverId,
            @RequestParam(required = false) String adoptionStatus,
            @RequestParam(required = false) MultipartFile adoptionAgreement,
            @RequestParam(required = false) String notes
    ) throws IOException {
        Adoption adoption = new Adoption();
        adoption.setSeekerId(seekerId);
        adoption.setGiverId(giverId);
        adoption.setAdoptionStatus(adoptionStatus != null ? adoptionStatus : "pending");
        adoption.setNotes(notes);

        if (adoptionAgreement != null && !adoptionAgreement.isEmpty()) {
            adoption.setAdoptionAgreement(adoptionAgreement.getBytes());
        }

        return adoptionService.saveAdoption(adoption);
    }

    // Get all adoptions
    @GetMapping("/all")
    public List<Adoption> getAllAdoptions() {
        return adoptionService.getAllAdoptions();
    }

    // Get adoption by ID
    @GetMapping("/{id}")
    public Optional<Adoption> getAdoptionById(@PathVariable int id) {
        return adoptionService.getAdoptionById(id);
    }

    // Get adoptions by seeker
    @GetMapping("/seeker/{seekerId}")
    public List<Adoption> getAdoptionsBySeeker(@PathVariable int seekerId) {
        return adoptionService.getAdoptionsBySeekerId(seekerId);
    }

    // Get adoptions by giver
    @GetMapping("/giver/{giverId}")
    public List<Adoption> getAdoptionsByGiver(@PathVariable int giverId) {
        return adoptionService.getAdoptionsByGiverId(giverId);
    }

    // Update adoption
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Adoption updateAdoption(
            @RequestParam Integer adoptionId,
            @RequestParam(required = false) Integer seekerId,
            @RequestParam(required = false) Integer giverId,
            @RequestParam(required = false) String adoptionStatus,
            @RequestParam(required = false) MultipartFile adoptionAgreement,
            @RequestParam(required = false) String notes
    ) throws IOException {
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

            return adoptionService.updateAdoption(adoption);
        }
        return null;
    }

    // Delete adoption
    @DeleteMapping("/delete/{id}")
    public void deleteAdoption(@PathVariable int id) {
        adoptionService.deleteAdoption(id);
    }

    // Serve adoption agreement document
    @GetMapping("/{id}/adoption-agreement")
    public @ResponseBody byte[] getAdoptionAgreement(@PathVariable int id) {
        Optional<Adoption> adoption = adoptionService.getAdoptionById(id);
        return adoption.map(Adoption::getAdoptionAgreement).orElse(null);
    }
}