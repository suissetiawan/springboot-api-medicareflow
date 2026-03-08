package com.dibimbing.medicareflow.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.request.WorkScheduleRequest;
import com.dibimbing.medicareflow.dto.response.WorkScheduleResponse;
import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.entity.WorkSchedule;
import com.dibimbing.medicareflow.enums.DayOfWeek;
import com.dibimbing.medicareflow.exception.ConflictException;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.DateHelper;
import com.dibimbing.medicareflow.repository.UserAccountRepository;
import com.dibimbing.medicareflow.repository.WorkScheduleRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.dibimbing.medicareflow.helper.RestPage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final UserAccountRepository userAccountRepository;


    @Cacheable(value = "work_schedules", key = "'all_' + #pageable.toString() + '_' + #username + '_' + #dayofweek")
    public Page<WorkScheduleResponse> getAllWorkSchedule(String username, DayOfWeek dayofweek, Pageable pageable) {
        Page<WorkSchedule> result = workScheduleRepository.findAllByFilter(username, dayofweek, pageable);
        return new RestPage<>(result.getContent().stream().map(this::mapToWorkScheduleResponse).toList(), pageable, result.getTotalElements());
    }

    @Transactional
    @CacheEvict(value = {"work_schedules", "work_schedule", "work_schedules_deleted"}, allEntries = true)
    public WorkScheduleResponse createWorkSchedule(WorkScheduleRequest request) {
        
        UserAccount user = userAccountRepository.findByUsername(request.getUsernameDoctor())
        .orElseThrow(() -> new NotFoundException("Doctor not found"));

        if (user.getDoctor() == null) {
            throw new NotFoundException("User is not a doctor");
        }

        Boolean isScheduleEmpty = workScheduleRepository.findByDoctorIdAndDayOfWeek(user.getDoctor().getId(), request.getDayOfWeek()).isEmpty();
        
        if (!isScheduleEmpty) {
            throw new ConflictException("Work schedule already exists");
        }
        
        WorkSchedule workSchedule = new WorkSchedule();
        workSchedule.setDoctor(user.getDoctor());
        workSchedule.setDayOfWeek(request.getDayOfWeek());
        workSchedule.setStartTime(request.getStartTime());
        workSchedule.setEndTime(request.getEndTime());

        WorkSchedule savedWorkSchedule = workScheduleRepository.save(workSchedule);

        return mapToWorkScheduleResponse(savedWorkSchedule);
    }

    @Transactional
    @CacheEvict(value = {"work_schedules", "work_schedule", "work_schedules_deleted"}, allEntries = true)
    public WorkScheduleResponse updateWorkSchedule(Long id, WorkScheduleRequest request) {
        
        WorkSchedule workSchedule = workScheduleRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Work schedule not found"));
        
        UserAccount user = userAccountRepository.findByUsername(request.getUsernameDoctor())
        .orElseThrow(() -> new NotFoundException("Doctor not found"));
        
        if (user.getDoctor() == null) {
            throw new NotFoundException("User is not a doctor");
        }

        Boolean isScheduleEmpty = workScheduleRepository.findByDoctorIdAndDayOfWeek(user.getDoctor().getId(), request.getDayOfWeek()).isEmpty();

        if (!isScheduleEmpty && workSchedule.getId() != id) {
            throw new ConflictException("Work schedule already exists");
        }
        
        workSchedule.setDoctor(user.getDoctor());
        workSchedule.setDayOfWeek(request.getDayOfWeek());
        workSchedule.setStartTime(request.getStartTime());
        workSchedule.setEndTime(request.getEndTime());

        WorkSchedule savedWorkSchedule = workScheduleRepository.save(workSchedule);
        return mapToWorkScheduleResponse(savedWorkSchedule);
    }

    @Transactional
    @CacheEvict(value = {"work_schedules", "work_schedule", "work_schedules_deleted"}, allEntries = true)
    public void deleteWorkSchedule(Long id) {
        WorkSchedule workSchedule = workScheduleRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Work schedule not found"));
        
        workSchedule.setDeletedAt(DateHelper.now());
        workScheduleRepository.save(workSchedule);
    }

    @Cacheable(value = "work_schedules_deleted", key = "'all_' + #pageable.toString()")
    public Page<WorkScheduleResponse> getAllDeleted(Pageable pageable) {
        Page<WorkSchedule> result = workScheduleRepository.findAllDeleted(pageable);
        return new RestPage<>(result.getContent().stream().map(this::mapToWorkScheduleResponse).toList(), pageable, result.getTotalElements());
    }

    @Transactional
    @CacheEvict(value = {"work_schedules", "work_schedule", "work_schedules_deleted"}, allEntries = true)
    public Boolean restore(Long id) {
        WorkSchedule workSchedule = workScheduleRepository.findByDeletedId(id)
                .orElseThrow(() -> new NotFoundException("Deleted work schedule not found"));

        workSchedule.setDeletedAt(null);
        workScheduleRepository.save(workSchedule);
        return true;
    }


    private WorkScheduleResponse mapToWorkScheduleResponse(WorkSchedule workSchedule) {
        WorkScheduleResponse response = new WorkScheduleResponse();
        response.setId(workSchedule.getId().toString());
        
        String doctorUsername = "Deleted Doctor";
        if (workSchedule.getDoctor() != null && workSchedule.getDoctor().getUserAccount() != null) {
            doctorUsername = workSchedule.getDoctor().getUserAccount().getUsername();
        }
        
        response.setDoctorUsername(doctorUsername);
        response.setDayOfWeek(workSchedule.getDayOfWeek().toString());
        response.setStartTime(workSchedule.getStartTime().toString());
        response.setEndTime(workSchedule.getEndTime().toString());
        return response;
    }
}


