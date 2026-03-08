package com.dibimbing.medicareflow.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Page<Appointment> findByPatientId(UUID patientId, Pageable pageable);
    Page<Appointment> findByPatientIdAndStatus(UUID patientId, com.dibimbing.medicareflow.enums.AppointmentStatus status, Pageable pageable);
    Page<Appointment> findByDoctorId(UUID doctorId, Pageable pageable);
    Page<Appointment> findByDoctorIdAndStatus(UUID doctorId, com.dibimbing.medicareflow.enums.AppointmentStatus status, Pageable pageable);
    Page<Appointment> findByStatus(com.dibimbing.medicareflow.enums.AppointmentStatus status, Pageable pageable);
    Optional<Appointment> findByTimeSlotId(Long timeSlotId);

    @Query(value = "SELECT * FROM appointment WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<Appointment> findByDeletedId(Long id);

    @Query(value = "SELECT * FROM appointment WHERE deleted_at IS NOT NULL", 
           countQuery = "SELECT count(*) FROM appointment WHERE deleted_at IS NOT NULL", 
           nativeQuery = true)
    Page<Appointment> findAllDeleted(Pageable pageable);

    @Query("SELECT a FROM Appointment a WHERE a.status = com.dibimbing.medicareflow.enums.AppointmentStatus.CONFIRMED " +
           "AND (a.timeSlot.slotDate < :currentDate " +
           "OR (a.timeSlot.slotDate = :currentDate AND a.timeSlot.endTime < :currentTime))")
    List<Appointment> findNoShowCandidates(@Param("currentDate") LocalDate currentDate, 
                                           @Param("currentTime") LocalTime currentTime);
}
