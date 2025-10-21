package com.pawdcast.pawdcast.application.service;

import com.pawdcast.pawdcast.application.model.PetProfile;
import com.pawdcast.pawdcast.application.repository.PetProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetProfileService {
    
    @Autowired
    private PetProfileRepository petProfileRepository;
    
    public PetProfile createPetProfile(PetProfile petProfile) {
        // Check if pet profile with same name already exists for this owner
        if (petProfileRepository.existsByOwnerIdAndName(petProfile.getOwnerId(), petProfile.getName())) {
            throw new RuntimeException("Pet with name '" + petProfile.getName() + "' already exists for this owner");
        }
        return petProfileRepository.save(petProfile);
    }
    
    public List<PetProfile> getPetProfilesByOwnerId(Integer ownerId) {
        return petProfileRepository.findByOwnerId(ownerId);
    }
    
    public Optional<PetProfile> getPetProfileById(Integer petId) {
        // This will automatically return Optional<PetProfile> since repository uses PetProfile
        return petProfileRepository.findById(petId);
    }
    
    public PetProfile updatePetProfile(Integer petId, PetProfile petProfileDetails) {
        Optional<PetProfile> optionalPetProfile = petProfileRepository.findById(petId);
        if (optionalPetProfile.isPresent()) {
            PetProfile petProfile = optionalPetProfile.get();
            
            // Update fields if provided
            if (petProfileDetails.getName() != null && !petProfileDetails.getName().trim().isEmpty()) {
                // Check if name is being changed and if new name already exists
                if (!petProfile.getName().equals(petProfileDetails.getName()) && 
                    petProfileRepository.existsByOwnerIdAndName(petProfile.getOwnerId(), petProfileDetails.getName())) {
                    throw new RuntimeException("Pet with name '" + petProfileDetails.getName() + "' already exists for this owner");
                }
                petProfile.setName(petProfileDetails.getName());
            }
            if (petProfileDetails.getType() != null) {
                petProfile.setType(petProfileDetails.getType());
            }
            if (petProfileDetails.getBreed() != null) {
                petProfile.setBreed(petProfileDetails.getBreed());
            }
            if (petProfileDetails.getDob() != null) {
                petProfile.setDob(petProfileDetails.getDob());
            }
            if (petProfileDetails.getMedicalRecords() != null) {
                petProfile.setMedicalRecords(petProfileDetails.getMedicalRecords());
            }
            if (petProfileDetails.getPhoto() != null) {
                petProfile.setPhoto(petProfileDetails.getPhoto());
            }
            
            return petProfileRepository.save(petProfile);
        }
        throw new RuntimeException("Pet profile not found with id: " + petId);
    }
    
    public void deletePetProfile(Integer petId) {
        if (petProfileRepository.existsById(petId)) {
            petProfileRepository.deleteById(petId);
        } else {
            throw new RuntimeException("Pet profile not found with id: " + petId);
        }
    }
    
    public boolean isPetProfileOwner(Integer petId, Integer ownerId) {
        Optional<PetProfile> petProfile = petProfileRepository.findById(petId);
        return petProfile.isPresent() && petProfile.get().getOwnerId().equals(ownerId);
    }
    
    public boolean existsByOwnerIdAndName(Integer ownerId, String name) {
        return petProfileRepository.existsByOwnerIdAndName(ownerId, name);
    }
}