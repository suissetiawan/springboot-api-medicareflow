package com.dibimbing.medicareflow.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.TimeSlot;
import com.dibimbing.medicareflow.repository.base.BaseRepository;

@Repository
public interface TimeSlotRepository extends BaseRepository<TimeSlot, Long> {

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.doctor.id = :doctorId AND ts.status = :status")
    List<TimeSlot> findByDoctorIdAndStatus(UUID doctorId, String status);

    @Query("SELECT COUNT(*) > 0 FROM TimeSlot ts WHERE ts.doctor.id = :doctorId AND ts.slotDate = :slotDate AND ts.startTime = :startTime")
    Boolean existsByDoctorAndSlotDateAndStartTime(UUID doctorId, LocalDate slotDate, LocalTime startTime);

    List<TimeSlot> findAllByDoctorIdAndSlotDate(UUID doctorId, LocalDate slotDate);

}
