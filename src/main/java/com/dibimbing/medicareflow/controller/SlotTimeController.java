package com.dibimbing.medicareflow.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.PaginationMeta;
import com.dibimbing.medicareflow.dto.response.TimeSlotResponse;
import com.dibimbing.medicareflow.enums.SlotStatus;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.SlotGeneratorService;
import com.dibimbing.medicareflow.service.SlotTimeService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/slot-time")
@RequiredArgsConstructor
@Tag(name = "Slot Time Management", description = "Endpoints for managing and generating individual time slots based on work schedules.")
public class SlotTimeController {

    private final SlotTimeService slotTimeService;
    private final SlotGeneratorService slotGeneratorService;

    @GetMapping
    public ResponseEntity<?> getAllTimeSlot(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate slotDate,
            @RequestParam(required = false) SlotStatus status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<TimeSlotResponse> result = slotTimeService.getAllTimeSlot(username, slotDate, status, pageable);

        PaginationMeta meta = PaginationMeta.builder()
                .page(result.getNumber() + 1)
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();

        return ResponseHelper.successOK(result.getContent(), "Successfully retrieved time slots", meta);
    }

    @PostMapping("/generate")
    public ResponseEntity<?> triggerManualSlotGeneration() {
        slotGeneratorService.autoGenerateSlots();
        return ResponseHelper.successOK(null, "Successfully triggered manual slot generation for the next 7 days");
    }
}
