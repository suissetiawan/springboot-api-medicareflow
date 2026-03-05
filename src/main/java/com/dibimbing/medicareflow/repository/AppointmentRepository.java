package com.dibimbing.medicareflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByPatientId(UUID patientId, Pageable pageable);
    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);
    Optional<Appointment> findByTimeSlotId(Long timeSlotId);

    @Query(value = "SELECT * FROM appointment WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<Appointment> findByDeletedId(Long id);

    @Query(value = "SELECT * FROM appointment WHERE deleted_at IS NOT NULL", 
           countQuery = "SELECT count(*) FROM appointment WHERE deleted_at IS NOT NULL", 
           nativeQuery = true)
    Page<Appointment> findAllDeleted(Pageable pageable);
}
