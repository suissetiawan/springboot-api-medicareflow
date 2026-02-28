package com.dibimbing.medicareflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dibimbing.medicareflow.entity.ConsultationType;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.entity.TimeSlot;
import com.dibimbing.medicareflow.entity.WorkSchedule;
import com.dibimbing.medicareflow.enums.DayOfWeek;
import com.dibimbing.medicareflow.repository.TimeSlotRepository;
import com.dibimbing.medicareflow.repository.WorkScheduleRepository;

@ExtendWith(MockitoExtension.class)
class SlotGeneratorServiceTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private WorkScheduleRepository workingScheduleRepository;

    @InjectMocks
    private SlotGeneratorService slotGeneratorService;

    private Doctor doctor;
    private WorkSchedule schedule;
    private ConsultationType type;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        
        type = new ConsultationType();
        type.setDurationMinutes(30);
        doctor.setServices(List.of(type));

        schedule = new WorkSchedule();
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));

        date = LocalDate.now();
    }

    @Test
    @SuppressWarnings("unchecked")
    void generateSlot_shouldGenerateSlots_whenNoExistingSlots() {
        when(timeSlotRepository.findAllByDoctorIdAndSlotDate(doctor.getId(), date)).thenReturn(Collections.emptyList());

        slotGeneratorService.generateSlot(doctor, schedule, date);

        ArgumentCaptor<List<TimeSlot>> captor = ArgumentCaptor.forClass(List.class);
        verify(timeSlotRepository, times(1)).saveAll(captor.capture());

        List<TimeSlot> savedSlots = captor.getValue();
        assertEquals(2, savedSlots.size());
        assertEquals(LocalTime.of(9, 0), savedSlots.get(0).getStartTime());
        assertEquals(LocalTime.of(9, 30), savedSlots.get(0).getEndTime());
        assertEquals(LocalTime.of(9, 30), savedSlots.get(1).getStartTime());
        assertEquals(LocalTime.of(10, 0), savedSlots.get(1).getEndTime());
    }

    @Test
    @SuppressWarnings("unchecked")
    void generateSlot_shouldSkipExistingSlots() {
        TimeSlot existingSlot = new TimeSlot();
        existingSlot.setStartTime(LocalTime.of(9, 0));
        when(timeSlotRepository.findAllByDoctorIdAndSlotDate(doctor.getId(), date)).thenReturn(List.of(existingSlot));

        slotGeneratorService.generateSlot(doctor, schedule, date);

        ArgumentCaptor<List<TimeSlot>> captor = ArgumentCaptor.forClass(List.class);
        verify(timeSlotRepository, times(1)).saveAll(captor.capture());

        List<TimeSlot> savedSlots = captor.getValue();
        assertEquals(1, savedSlots.size());
        assertEquals(LocalTime.of(9, 30), savedSlots.get(0).getStartTime());
    }

    @Test
    void generateSlot_shouldNotSave_whenNoNewSlots() {
        TimeSlot slot1 = new TimeSlot();
        slot1.setStartTime(LocalTime.of(9, 0));
        TimeSlot slot2 = new TimeSlot();
        slot2.setStartTime(LocalTime.of(9, 30));
        when(timeSlotRepository.findAllByDoctorIdAndSlotDate(doctor.getId(), date)).thenReturn(List.of(slot1, slot2));

        slotGeneratorService.generateSlot(doctor, schedule, date);

        verify(timeSlotRepository, never()).saveAll(anyList());
    }

    @Test
    void generateSlot_shouldThrowException_whenInputsAreNull() {
        assertThrows(IllegalArgumentException.class, () -> slotGeneratorService.generateSlot(null, schedule, date));
    }

    @Test
    void generateSlot_shouldThrowException_whenDurationIsInvalid() {
        type.setDurationMinutes(0);
        assertThrows(IllegalArgumentException.class, () -> slotGeneratorService.generateSlot(doctor, schedule, date));
    }

    @Test
    void autoGenerateSlots_shouldGenerateForNext7Days() {
        // Arrange
        schedule.setDoctor(doctor);
        
        when(workingScheduleRepository.findByDayOfWeek(any(DayOfWeek.class)))
                .thenReturn(List.of(schedule));
        
        // Mocking findAllByDoctorIdAndSlotDate to return empty list for all dates
        when(timeSlotRepository.findAllByDoctorIdAndSlotDate(any(UUID.class), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        // Act
        slotGeneratorService.autoGenerateSlots();

        // Assert
        // verify workingScheduleRepository is called 7 times (one for each day)
        verify(workingScheduleRepository, times(7)).findByDayOfWeek(any(DayOfWeek.class));
        
        // each call generates 2 slots (9:00, 9:30)
        // total 7 days * 2 slots = 14 slots
        // saveAll should be called 7 times (once per day with new slots)
        verify(timeSlotRepository, times(7)).saveAll(anyList());
    }
}
