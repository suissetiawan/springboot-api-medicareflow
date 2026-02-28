package com.dibimbing.medicareflow.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.entity.ConsultationType;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.entity.TimeSlot;
import com.dibimbing.medicareflow.entity.WorkSchedule;
import com.dibimbing.medicareflow.enums.DayOfWeek;
import com.dibimbing.medicareflow.enums.DoctorStatus;
import com.dibimbing.medicareflow.enums.SlotStatus;
import com.dibimbing.medicareflow.repository.TimeSlotRepository;
import com.dibimbing.medicareflow.repository.WorkScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlotGeneratorService {

    private final TimeSlotRepository timeSlotRepository;
    private final WorkScheduleRepository workingScheduleRepository;

    @Transactional
    public void generateSlot(
            Doctor doctor,
            WorkSchedule schedule,
            LocalDate date) {
        
        if (doctor == null || schedule == null || date == null) {
            throw new IllegalArgumentException("Add data Doctor and working schedule");
        }

        // Use the first service associated with the doctor for duration
        if (doctor.getServices() == null || doctor.getServices().isEmpty()) {
            log.warn("Doctor {} has no services assigned. Cannot generate slots.", doctor.getName());
            return;
        }

        ConsultationType type = doctor.getServices().get(0);
        int duration = type.getDurationMinutes();
        if (duration <= 0) {
            throw new IllegalArgumentException("Consultation type duration must be greater than zero");
        }

        LocalTime currentTime = schedule.getStartTime();
        Set<LocalTime> existingStartTimes = timeSlotRepository.findByDoctorIdAndSlotDate(doctor.getId(), date)
                .stream()
                .map(TimeSlot::getStartTime)
                .collect(Collectors.toSet());

        List<TimeSlot> newSlots = new ArrayList<>();

        while(!currentTime.plusMinutes(duration).isAfter(schedule.getEndTime())) {

            if(!existingStartTimes.contains(currentTime)) {
                TimeSlot slot = new TimeSlot();
                slot.setDoctor(doctor);
                slot.setSlotDate(date);
                slot.setStartTime(currentTime);
                slot.setEndTime(currentTime.plusMinutes(duration));
                slot.setStatus(SlotStatus.AVAILABLE);
                newSlots.add(slot);
            }
            currentTime = currentTime.plusMinutes(duration);
        }

        if (!newSlots.isEmpty()) {
            timeSlotRepository.saveAll(newSlots);
            log.info("Generated {} new slots for doctor {} on {}", newSlots.size(), doctor.getName(), date);
        }
    }

    @Scheduled(cron = "${app.slot.generation-cron}")
    @Transactional
    public void autoGenerateSlots() {
        log.info("Starting automated slot generation for the next 7 days");
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            LocalDate targetDate = today.plusDays(i);
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(targetDate.getDayOfWeek().name());
            List<WorkSchedule> schedules = workingScheduleRepository.findByDayOfWeek(dayOfWeek);
            
            for (WorkSchedule schedule : schedules) {
                Doctor doctor = schedule.getDoctor();
                if (doctor.getStatus().equals(DoctorStatus.ACTIVE)) {
                    generateSlot(doctor, schedule, targetDate);
                }
            }
        }
        log.info("Automated slot generation completed");
    }
}
