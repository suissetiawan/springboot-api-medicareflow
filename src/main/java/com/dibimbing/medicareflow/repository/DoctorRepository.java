package com.dibimbing.medicareflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dibimbing.medicareflow.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByUserAccountId(UUID userAccountId);

}
