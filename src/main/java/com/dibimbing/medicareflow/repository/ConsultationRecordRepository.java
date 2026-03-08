package com.dibimbing.medicareflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.ConsultationRecord;

@Repository
public interface ConsultationRecordRepository extends JpaRepository<ConsultationRecord, Long> {
    Optional<ConsultationRecord> findByAppointmentId(Long appointmentId);

    Page<ConsultationRecord> findByAppointmentPatientUserAccountId(UUID userAccountId, Pageable pageable);

    Page<ConsultationRecord> findByAppointmentDoctorUserAccountId(UUID userAccountId, Pageable pageable);

    Page<ConsultationRecord> findByAppointmentDoctorUserAccountUsername(String doctorUsername, Pageable pageable);
    
    Page<ConsultationRecord> findByAppointmentPatientUserAccountUsername(String patientUsername, Pageable pageable);

    @Query(value = "SELECT * FROM consultation_record WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<ConsultationRecord> findByDeletedId(Long id);

    @Query(value = "SELECT * FROM consultation_record WHERE deleted_at IS NOT NULL", 
           countQuery = "SELECT count(*) FROM consultation_record WHERE deleted_at IS NOT NULL", 
           nativeQuery = true)
    Page<ConsultationRecord> findAllDeleted(Pageable pageable);
}
