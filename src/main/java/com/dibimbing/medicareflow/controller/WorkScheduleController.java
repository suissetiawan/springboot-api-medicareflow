package com.dibimbing.medicareflow.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dibimbing.medicareflow.dto.PaginationMeta;
import com.dibimbing.medicareflow.dto.request.WorkScheduleRequest;
import com.dibimbing.medicareflow.dto.response.WorkScheduleResponse;
import com.dibimbing.medicareflow.enums.DayOfWeek;
import com.dibimbing.medicareflow.helper.ResponseHelper;
import com.dibimbing.medicareflow.service.WorkScheduleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/work-schedule")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService workScheduleService;

    @PostMapping
    public ResponseEntity<?> createWorkSchedule(@RequestBody WorkScheduleRequest request) {
        WorkScheduleResponse response = workScheduleService.createWorkSchedule(request);

        return ResponseHelper.successOK(response, "Success create work schedule");
    }

    @GetMapping
    public ResponseEntity<?> getAllWorkSchedule(
        @RequestParam(required = false) String username, 
        @RequestParam(required = false) String dayofweek,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        
        DayOfWeek day = null;
        if (dayofweek != null && !dayofweek.isBlank()) {
            try {
                day = DayOfWeek.valueOf(dayofweek.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseHelper.error("Invalid day of week: " + dayofweek, HttpStatus.BAD_REQUEST);
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<WorkScheduleResponse> schedule = workScheduleService.getAllWorkSchedule(username, day, pageable);
        
        PaginationMeta meta = PaginationMeta.builder()
                .page(schedule.getNumber())
                .size(schedule.getSize())
                .totalElements(schedule.getTotalElements())
                .totalPages(schedule.getTotalPages())
                .build();

        return ResponseHelper.successOK(schedule.getContent(), "Success get all work schedule", meta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkSchedule(@PathVariable Long id, @RequestBody WorkScheduleRequest request) {
        WorkScheduleResponse response = workScheduleService.updateWorkSchedule(id, request);
        return ResponseHelper.successOK(response, "Success update work schedule");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkSchedule(@PathVariable Long id) {
        workScheduleService.deleteWorkSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
