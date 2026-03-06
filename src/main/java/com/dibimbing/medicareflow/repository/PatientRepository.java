package com.dibimbing.medicareflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dibimbing.medicareflow.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    @Query("SELECT p FROM Patient p WHERE p.userAccount.id = :userAccountId")
    Optional<Patient> findByUserAccountId(java.util.UUID userAccountId);



}
