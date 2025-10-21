package com.pawdcast.pawdcast.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pawdcast.pawdcast.application.model.Adoption;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Integer> {

    // Find all adoptions for a seeker
    List<Adoption> findBySeekerId(Integer seekerId);

    // Find all adoptions for a giver
    List<Adoption> findByGiverId(Integer giverId);

    // Optional: find adoptions by status
    List<Adoption> findByAdoptionStatus(String adoptionStatus);
}
