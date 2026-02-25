package com.dibimbing.medicareflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.repository.base.BaseRepository;

public interface DoctorRepository extends BaseRepository<Doctor, UUID> {

    @Query("SELECT d FROM Doctor d JOIN FETCH d.userAccount WHERE d.id = :id")
    Optional<Doctor> findById(UUID id);

}
