package com.pawdcast.pawdcast.application.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pawdcast.pawdcast.application.model.Pet;
import com.pawdcast.pawdcast.application.service.PetService;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*") // allows frontend to access
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Get all pets
    @GetMapping
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    // Get pets by type (dog or cat)
    @GetMapping("/type/{type}")
    public List<Pet> getPetsByType(@PathVariable String type) {
        return petService.getPetsByType(type);
    }
}