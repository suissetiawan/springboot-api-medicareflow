package com.dibimbing.medicareflow.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    Page<Appointment> findByPatientId(UUID patientId, Pageable pageable);
    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);
}
