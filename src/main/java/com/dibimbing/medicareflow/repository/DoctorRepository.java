package com.dibimbing.medicareflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dibimbing.medicareflow.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    @Query("SELECT d FROM Doctor d WHERE d.userAccount.id = :userAccountId")
    Optional<Doctor> findByUserAccountId(java.util.UUID userAccountId);

    @Query("SELECT d FROM Doctor d WHERE d.userAccount.username = :username")
    Optional<Doctor> findByUserAccountUsername(String username);

}
