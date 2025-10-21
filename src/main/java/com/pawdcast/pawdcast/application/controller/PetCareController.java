package com.pawdcast.pawdcast.application.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pawdcast.pawdcast.application.model.PetCareGuide;
import com.pawdcast.pawdcast.application.service.PetCareService;

@RestController
public class PetCareController {

    private final PetCareService petCareService;

    public PetCareController(PetCareService petCareService) {
        this.petCareService = petCareService;
    }

    @GetMapping("/api/pet-care")
    public PetCareGuide getPetCareGuide() throws IOException {
        return petCareService.loadPetCareGuide();
    }
}
