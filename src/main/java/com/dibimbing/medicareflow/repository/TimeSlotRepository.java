package com.dibimbing.medicareflow.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.TimeSlot;
import com.dibimbing.medicareflow.enums.SlotStatus;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("SELECT ts FROM TimeSlot ts WHERE " +
           "(:username IS NULL OR ts.doctor.userAccount.username LIKE %:username%) AND " +
           "(:slotDate IS NULL OR ts.slotDate = :slotDate) AND " +
           "(:status IS NULL OR ts.status = :status)")
    Page<TimeSlot> findAllByFilter(String username, LocalDate slotDate, SlotStatus status, Pageable pageable);

    List<TimeSlot> findByDoctorId(UUID doctorId);

    List<TimeSlot> findBySlotDate(LocalDate slotDate);

    List<TimeSlot> findByStatus(SlotStatus status);

    List<TimeSlot> findByDoctorAndSlotDate(UUID doctorId, LocalDate slotDate);

    List<TimeSlot> findByDoctorAndSlotDateAndStatus(
        UUID doctorId,
        LocalDate slotDate,
        SlotStatus status
    );

    List<TimeSlot> findByDoctorAndStatus(
        UUID doctorId,
        SlotStatus status
    );

    List<TimeSlot> findBySlotDateAndStatus(
        LocalDate slotDate,
        SlotStatus status
    );
}
