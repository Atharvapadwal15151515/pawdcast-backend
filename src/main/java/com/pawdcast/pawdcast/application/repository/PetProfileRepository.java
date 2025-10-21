package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.PetProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetProfileRepository extends JpaRepository<PetProfile, Integer> {
    
    List<PetProfile> findByOwnerId(Integer ownerId);
    
    boolean existsByOwnerIdAndName(Integer ownerId, String name);
}