package com.dibimbing.medicareflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.ConsultationRecord;

@Repository
public interface ConsultationRecordRepository extends JpaRepository<ConsultationRecord, Long> {
    Optional<ConsultationRecord> findByAppointmentId(Long appointmentId);
}
