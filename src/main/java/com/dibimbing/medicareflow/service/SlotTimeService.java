package com.dibimbing.medicareflow.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.response.TimeSlotResponse;
import com.dibimbing.medicareflow.entity.TimeSlot;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.enums.AppointmentStatus;
import com.dibimbing.medicareflow.enums.DayOfWeek;
import com.dibimbing.medicareflow.enums.SlotStatus;
import com.dibimbing.medicareflow.exception.ConflictException;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.SecurityHelper;
import com.dibimbing.medicareflow.repository.AppointmentRepository;
import com.dibimbing.medicareflow.repository.TimeSlotRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotTimeService {

    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserAccountRepository userAccountRepository;

    public Page<TimeSlotResponse> getAllTimeSlot(String username, LocalDate slotDate, SlotStatus status, DayOfWeek dayOfWeek, Pageable pageable) {
        String dayOfWeekStr = dayOfWeek != null ? dayOfWeek.name() : null;
        Page<TimeSlot> slots = timeSlotRepository.findAllByFilter(username, slotDate, status, dayOfWeekStr, pageable);
        return slots.map(this::mapToTimeSlotResponse);
    }

    @Transactional
    public void cancelTimeSlot(Long slotId) {
        TimeSlot timeSlot = timeSlotRepository.findById(slotId)
                .orElseThrow(() -> new NotFoundException("Time slot not found"));

        if (timeSlot.getStatus() == SlotStatus.CANCELLED) {
            throw new ConflictException("Time slot is already cancelled");
        }

        String currentUsername = SecurityHelper.getCurrentUsername();
        UserAccount currentUser = userAccountRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Permission check: Admin or the Doctor who owns the slot
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        boolean isOwner = currentUser.getRole().name().equals("DOCTOR") && 
                          timeSlot.getDoctor().getUserAccount().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ConflictException("You are not authorized to cancel this slot");
        }

        // If booked, cancel the appointment
        if (timeSlot.getStatus() == SlotStatus.BOOKED) {
            appointmentRepository.findByTimeSlotId(slotId).ifPresent(appointment -> {
                appointment.setStatus(AppointmentStatus.CANCELLED);
                appointmentRepository.save(appointment);
            });
        }

        timeSlot.setStatus(SlotStatus.CANCELLED);
        timeSlotRepository.save(timeSlot);
    }

    private TimeSlotResponse mapToTimeSlotResponse(TimeSlot timeSlot) {
        TimeSlotResponse response = new TimeSlotResponse();
        response.setId(timeSlot.getId().toString());
        response.setDoctorUsername(timeSlot.getDoctor().getUserAccount().getUsername());
        response.setSlotDate(timeSlot.getSlotDate().toString());
        response.setStartTime(timeSlot.getStartTime().toString());
        response.setEndTime(timeSlot.getEndTime().toString());
        response.setStatus(timeSlot.getStatus().name());
        response.setDayOfWeek(timeSlot.getSlotDate().getDayOfWeek().name());
        return response;
    }
}
