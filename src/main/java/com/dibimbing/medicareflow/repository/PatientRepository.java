package com.dibimbing.medicareflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.dibimbing.medicareflow.entity.Patient;
import com.dibimbing.medicareflow.repository.base.BaseRepository;

public interface PatientRepository extends BaseRepository<Patient, UUID> {
    
    @Query("SELECT p FROM Patient p JOIN FETCH p.userAccount WHERE p.id = :id")
    Optional<Patient> findById(UUID id);

}
