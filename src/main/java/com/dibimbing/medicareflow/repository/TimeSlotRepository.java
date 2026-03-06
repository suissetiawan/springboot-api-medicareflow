package com.dibimbing.medicareflow.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
           "(:status IS NULL OR ts.status = :status) AND " +
           "(:dayOfWeek IS NULL OR UPPER(FUNCTION('DAYNAME', ts.slotDate)) = :dayOfWeek) AND " +
           "(ts.slotDate >= :today)")
    Page<TimeSlot> findAllByFilter(String username, LocalDate slotDate, SlotStatus status, String dayOfWeek, LocalDate today, Pageable pageable);

    List<TimeSlot> findByDoctorId(UUID doctorId);

    List<TimeSlot> findBySlotDate(LocalDate slotDate);

    List<TimeSlot> findByStatus(SlotStatus status);

    List<TimeSlot> findByDoctorIdAndSlotDate(UUID doctorId, LocalDate slotDate);

    List<TimeSlot> findByDoctorIdAndSlotDateAndStatus(
        UUID doctorId,
        LocalDate slotDate,
        SlotStatus status
    );

    List<TimeSlot> findByDoctorIdAndStatus(
        UUID doctorId,
        SlotStatus status
    );

    List<TimeSlot> findBySlotDateAndStatus(
        LocalDate slotDate,
        SlotStatus status
    );

    @Query(value = "SELECT * FROM time_slot WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<TimeSlot> findByDeletedId(Long id);

    @Query(value = "SELECT * FROM time_slot WHERE deleted_at IS NOT NULL", 
           countQuery = "SELECT count(*) FROM time_slot WHERE deleted_at IS NOT NULL", 
           nativeQuery = true)
    Page<TimeSlot> findAllDeleted(Pageable pageable);
}
