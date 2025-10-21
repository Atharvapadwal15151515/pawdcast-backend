package com.pawdcast.pawdcast.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pawdcast.pawdcast.application.model.AdoptionSeeker;
import com.pawdcast.pawdcast.application.repository.AdoptionSeekerRepository;

@Service
public class AdoptionSeekerService {

    @Autowired
    private AdoptionSeekerRepository adoptionSeekerRepository;

    // Save a new adoption seeker
    public AdoptionSeeker saveSeeker(AdoptionSeeker seeker) {
        return adoptionSeekerRepository.save(seeker);
    }

    // Get all seekers
    public List<AdoptionSeeker> getAllSeekers() {
        return adoptionSeekerRepository.findAll();
    }

    // Get seeker by ID
    public Optional<AdoptionSeeker> getSeekerById(int seekerId) {
        return adoptionSeekerRepository.findById(seekerId);
    }

    // Update seeker
    public AdoptionSeeker updateSeeker(AdoptionSeeker seeker) {
        return adoptionSeekerRepository.save(seeker);
    }

    // Delete seeker
    public void deleteSeeker(int seekerId) {
        adoptionSeekerRepository.deleteById(seekerId);
    }

    // Check if seeker exists
    public boolean seekerExists(int seekerId) {
        return adoptionSeekerRepository.existsById(seekerId);
    }
}
