package com.pawdcast.pawdcast.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pawdcast.pawdcast.application.model.PetGiver;

@Repository
public interface PetGiverRepository extends JpaRepository<PetGiver, Integer> {
    
    // Find all pets that are available for adoption
    List<PetGiver> findByAdoptionStatus(String adoptionStatus);
}

