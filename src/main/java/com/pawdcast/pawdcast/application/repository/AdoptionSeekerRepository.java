package com.pawdcast.pawdcast.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pawdcast.pawdcast.application.model.AdoptionSeeker;

@Repository
public interface AdoptionSeekerRepository extends JpaRepository<AdoptionSeeker, Integer> {

    // Optional: find seeker by ID (already provided by JpaRepository)
    AdoptionSeeker findBySeekerId(int seekerId);
}
