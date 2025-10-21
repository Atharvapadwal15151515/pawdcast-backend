package com.pawdcast.pawdcast.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pawdcast.pawdcast.application.model.PetGiver;
import com.pawdcast.pawdcast.application.repository.PetGiverRepository;

@Service
public class PetGiverService {

    @Autowired
    private PetGiverRepository petGiverRepository;

    // Save a new pet giver record
    public PetGiver savePetGiver(PetGiver petGiver) {
        return petGiverRepository.save(petGiver);
    }

    // Get all pets
    public List<PetGiver> getAllPets() {
        return petGiverRepository.findAll();
    }

    // Get all available pets
    public List<PetGiver> getAvailablePets() {
        return petGiverRepository.findByAdoptionStatus("available");
    }

    // Get pet by ID
    public Optional<PetGiver> getPetById(int id) {
        return petGiverRepository.findById(id);
    }

    // Update pet details
    public PetGiver updatePet(PetGiver petGiver) {
        return petGiverRepository.save(petGiver);
    }

    // Delete pet
    public void deletePet(int id) {
        petGiverRepository.deleteById(id);
    }
}
