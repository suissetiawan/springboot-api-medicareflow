package com.dibimbing.medicareflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.dibimbing.medicareflow.dto.response.TimeSlotResponse;
import com.dibimbing.medicareflow.entity.Appointment;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.entity.TimeSlot;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.enums.AppointmentStatus;
import com.dibimbing.medicareflow.enums.DayOfWeek;
import com.dibimbing.medicareflow.enums.Role;
import com.dibimbing.medicareflow.enums.SlotStatus;
import com.dibimbing.medicareflow.helper.SecurityHelper;
import com.dibimbing.medicareflow.repository.AppointmentRepository;
import com.dibimbing.medicareflow.repository.TimeSlotRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

@ExtendWith(MockitoExtension.class)
class SlotTimeServiceTest {

    @Mock
    private TimeSlotRepository timeSlotRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private SlotTimeService slotTimeService;

    private Doctor doctor;
    private UserAccount userAccount;
    private TimeSlot timeSlot;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount();
        userAccount.setId(UUID.randomUUID());
        userAccount.setUsername("doctor1");
        userAccount.setRole(Role.DOCTOR);

        doctor = new Doctor();
        doctor.setId(UUID.randomUUID());
        doctor.setUserAccount(userAccount);

        date = LocalDate.of(2025, 3, 5); // A Wednesday

        timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setDoctor(doctor);
        timeSlot.setSlotDate(date);
        timeSlot.setStartTime(LocalTime.of(9, 0));
        timeSlot.setEndTime(LocalTime.of(9, 30));
        timeSlot.setStatus(SlotStatus.AVAILABLE);
    }

    @Test
    void getAllTimeSlot_shouldMapDayOfWeekCorrectly() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TimeSlot> page = new PageImpl<>(List.of(timeSlot));

        when(timeSlotRepository.findAllByFilter(anyString(), any(), any(), any(), any())).thenReturn(page);

        Page<TimeSlotResponse> result = slotTimeService.getAllTimeSlot("doctor1", date, SlotStatus.AVAILABLE, null, pageable);

        assertEquals(1, result.getContent().size());
        TimeSlotResponse response = result.getContent().get(0);
        assertEquals("WEDNESDAY", response.getDayOfWeek());
        assertEquals("AVAILABLE", response.getStatus());
        assertEquals("2025-03-05", response.getSlotDate());
    }

    @Test
    void getAllTimeSlot_withDayOfWeekFilter_shouldPassToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TimeSlot> page = new PageImpl<>(List.of(timeSlot));

        when(timeSlotRepository.findAllByFilter(anyString(), any(), any(), anyString(), any())).thenReturn(page);

        slotTimeService.getAllTimeSlot("doctor1", null, null, DayOfWeek.WEDNESDAY, pageable);

        verify(timeSlotRepository).findAllByFilter("doctor1", null, null, "WEDNESDAY", pageable);
    }

    @Test
    void cancelTimeSlot_shouldCancelAvailableSlot() {
        try (MockedStatic<SecurityHelper> mockedSecurity = Mockito.mockStatic(SecurityHelper.class)) {
            mockedSecurity.when(SecurityHelper::getCurrentUsername).thenReturn("doctor1");
            when(userAccountRepository.findByUsername("doctor1")).thenReturn(Optional.of(userAccount));
            when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));

            slotTimeService.cancelTimeSlot(1L);

            assertEquals(SlotStatus.CANCELLED, timeSlot.getStatus());
            verify(timeSlotRepository).save(timeSlot);
        }
    }

    @Test
    void cancelTimeSlot_shouldCancelBookedSlotAndAppointment() {
        timeSlot.setStatus(SlotStatus.BOOKED);
        Appointment appointment = new Appointment();
        appointment.setStatus(AppointmentStatus.PENDING);

        try (MockedStatic<SecurityHelper> mockedSecurity = Mockito.mockStatic(SecurityHelper.class)) {
            mockedSecurity.when(SecurityHelper::getCurrentUsername).thenReturn("doctor1");
            when(userAccountRepository.findByUsername("doctor1")).thenReturn(Optional.of(userAccount));
            when(timeSlotRepository.findById(1L)).thenReturn(Optional.of(timeSlot));
            when(appointmentRepository.findByTimeSlotId(1L)).thenReturn(Optional.of(appointment));

            slotTimeService.cancelTimeSlot(1L);

            assertEquals(SlotStatus.CANCELLED, timeSlot.getStatus());
            assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
            verify(timeSlotRepository).save(timeSlot);
            verify(appointmentRepository).save(appointment);
        }
    }
}
