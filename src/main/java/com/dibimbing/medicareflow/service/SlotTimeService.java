package com.dibimbing.medicareflow.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.response.TimeSlotResponse;
import com.dibimbing.medicareflow.entity.TimeSlot;
import com.dibimbing.medicareflow.enums.SlotStatus;
import com.dibimbing.medicareflow.repository.TimeSlotRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlotTimeService {

    private final TimeSlotRepository timeSlotRepository;

    public Page<TimeSlotResponse> getAllTimeSlot(String username, LocalDate slotDate, SlotStatus status, Pageable pageable) {
        Page<TimeSlot> slots = timeSlotRepository.findAllByFilter(username, slotDate, status, pageable);
        return slots.map(this::mapToTimeSlotResponse);
    }

    private TimeSlotResponse mapToTimeSlotResponse(TimeSlot timeSlot) {
        TimeSlotResponse response = new TimeSlotResponse();
        response.setId(timeSlot.getId().toString());
        response.setDoctorUsername(timeSlot.getDoctor().getUserAccount().getUsername());
        response.setSlotDate(timeSlot.getSlotDate().toString());
        response.setStartTime(timeSlot.getStartTime().toString());
        response.setEndTime(timeSlot.getEndTime().toString());
        response.setStatus(timeSlot.getStatus().name());
        return response;
    }

}
