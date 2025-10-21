package com.pawdcast.pawdcast.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pawdcast.pawdcast.application.model.Adoption;
import com.pawdcast.pawdcast.application.repository.AdoptionRepository;

@Service
public class AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;

    // Save adoption record
    public Adoption saveAdoption(Adoption adoption) {
        return adoptionRepository.save(adoption);
    }

    // Get all adoptions
    public List<Adoption> getAllAdoptions() {
        return adoptionRepository.findAll();
    }

    // Get adoption by ID
    public Optional<Adoption> getAdoptionById(int adoptionId) {
        return adoptionRepository.findById(adoptionId);
    }

    // Get all adoptions for a seeker
    public List<Adoption> getAdoptionsBySeekerId(int seekerId) {
        return adoptionRepository.findBySeekerId(seekerId);
    }

    // Get all adoptions for a giver
    public List<Adoption> getAdoptionsByGiverId(int giverId) {
        return adoptionRepository.findByGiverId(giverId);
    }

    // Update adoption
    public Adoption updateAdoption(Adoption adoption) {
        return adoptionRepository.save(adoption);
    }

    // Delete adoption
    public void deleteAdoption(int adoptionId) {
        adoptionRepository.deleteById(adoptionId);
    }
}

