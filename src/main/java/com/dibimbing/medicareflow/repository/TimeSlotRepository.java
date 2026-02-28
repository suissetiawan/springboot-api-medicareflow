package com.dibimbing.medicareflow.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.TimeSlot;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.doctor.id = :doctorId AND ts.slotDate = :slotDate")
    List<TimeSlot> findAllByDoctorIdAndSlotDate(UUID doctorId, LocalDate slotDate);

}
