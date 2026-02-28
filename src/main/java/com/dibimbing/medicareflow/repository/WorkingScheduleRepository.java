package com.dibimbing.medicareflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.WorkingSchedule;
import com.dibimbing.medicareflow.enums.DayofWeek;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface WorkingScheduleRepository extends JpaRepository<WorkingSchedule, Long> {

    @Query("SELECT ws FROM WorkingSchedule ws WHERE ws.dayOfWeek = :dayOfWeek")
    List<WorkingSchedule> findByDayOfWeek(DayofWeek dayOfWeek);

}
