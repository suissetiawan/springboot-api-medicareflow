package com.dibimbing.medicareflow.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.WorkSchedule;
import com.dibimbing.medicareflow.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {

    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.dayOfWeek = :dayOfWeek")
    List<WorkSchedule> findByDayOfWeek(DayOfWeek dayOfWeek);

    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.doctor.id = :doctorId")
    List<WorkSchedule> findByDoctorId(UUID doctorId);

    @Query("SELECT ws FROM WorkSchedule ws WHERE ws.doctor.id = :doctorId AND ws.dayOfWeek = :dayOfWeek")
    List<WorkSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, DayOfWeek dayOfWeek);

    

}
