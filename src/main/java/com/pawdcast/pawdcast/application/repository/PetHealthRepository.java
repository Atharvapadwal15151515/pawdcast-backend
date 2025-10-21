package com.pawdcast.pawdcast.application.repository;

import com.pawdcast.pawdcast.application.model.PetHealth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PetHealthRepository extends JpaRepository<PetHealth, Long> {
	List<PetHealth> findByPetPetIdOrderByEntryDateDesc(Integer petId);
}
