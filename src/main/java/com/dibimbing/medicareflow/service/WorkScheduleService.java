package com.dibimbing.medicareflow.service;

import java.util.List;
import java.util.stream.Collectors;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final UserAccountRepository userAccountRepository;


    public List<WorkScheduleResponse> getAllWorkSchedule(String username, DayOfWeek dayofweek) {

        UserAccount user = (username == null || username.isBlank()) ? null : 
            userAccountRepository.findByUsername(username).orElse(null);

        if (user == null) {
            if (dayofweek == null) {
                return workScheduleRepository.findAll().stream().map(this::mapToWorkScheduleResponse).collect(Collectors.toList());
            } else {
                return workScheduleRepository.findByDayOfWeek(dayofweek).stream().map(this::mapToWorkScheduleResponse).collect(Collectors.toList());
            }
        }

        if (user.getDoctor() == null) {
            throw new NotFoundException("User is not a doctor");
        }

        if (dayofweek == null) {
            return workScheduleRepository.findByDoctorId(user.getDoctor().getId()).stream().map(this::mapToWorkScheduleResponse).collect(Collectors.toList());
        } else {
            return workScheduleRepository.findByDoctorIdAndDayOfWeek(user.getDoctor().getId(), dayofweek).stream().map(this::mapToWorkScheduleResponse).collect(Collectors.toList());
        }
    }

    @Transactional
    public WorkScheduleResponse createWorkSchedule(WorkScheduleRequest request) {
        
        UserAccount user = userAccountRepository.findByUsername(request.getUsernameDoctor())
        .orElseThrow(() -> new NotFoundException("Doctor not found"));

        Boolean isScheduleEmpty = workScheduleRepository.findByDoctorIdAndDayOfWeek(user.getDoctor().getId(), request.getDayOfWeek()).isEmpty();
        
        if (!isScheduleEmpty) {
            throw new ConflictException("Work schedule already exists");
        }
        
        if (user.getDoctor() == null) {
            throw new NotFoundException("User is not a doctor");
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
    public void deleteWorkSchedule(Long id) {
        WorkSchedule workSchedule = workScheduleRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Work schedule not found"));
        
        workSchedule.setDeletedAt(DateHelper.now());
        workScheduleRepository.save(workSchedule);
    }


    private WorkScheduleResponse mapToWorkScheduleResponse(WorkSchedule workSchedule) {
        WorkScheduleResponse response = new WorkScheduleResponse();
        response.setId(workSchedule.getId().toString());
        response.setDoctorUsername(workSchedule.getDoctor().getUserAccount().getUsername());
        response.setDayOfWeek(workSchedule.getDayOfWeek().toString());
        response.setStartTime(workSchedule.getStartTime().toString());
        response.setEndTime(workSchedule.getEndTime().toString());
        return response;
    }
}


