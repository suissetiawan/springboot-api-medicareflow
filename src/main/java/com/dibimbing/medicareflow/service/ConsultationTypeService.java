package com.dibimbing.medicareflow.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.request.ConsultationStatusRequest;
import com.dibimbing.medicareflow.dto.request.ConsultationTypeRequest;
import com.dibimbing.medicareflow.dto.response.ConsultationTypeResponse;
import com.dibimbing.medicareflow.entity.ConsultationType;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.DateHelper;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.repository.ConsultationTypeRepository;
import com.dibimbing.medicareflow.repository.DoctorRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.dibimbing.medicareflow.helper.RestPage;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationTypeService {

    private final ConsultationTypeRepository consultationTypeRepository;
    private final DoctorRepository doctorRepository;

    @Cacheable(value = "consultation_types", key = "'doctor_' + #username")
    public List<ConsultationTypeResponse> getConsultationTypesByDoctorUsername(String username) {
        Doctor doctor = doctorRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new NotFoundException("Doctor not found with username: " + username));

        return doctor.getServices().stream()
                .map(this::mapToConsultationTypeResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "consultation_types", key = "'all_' + #pageable.toString()")
    public Page<ConsultationTypeResponse> getAllConsultationTypes(Pageable pageable) {
        Page<ConsultationType> type = consultationTypeRepository.findAll(pageable);
        return new RestPage<>(type.getContent().stream().map(this::mapToConsultationTypeResponse).toList(), pageable, type.getTotalElements());
    }

    @Cacheable(value = "consultation_type", key = "#id")
    public ConsultationTypeResponse getConsultationTypeById(Long id) {
        ConsultationType type = consultationTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Consultation type not found"));

        return mapToConsultationTypeResponse(type);
    }

    @Transactional
    @CacheEvict(value = {"consultation_types", "consultation_type"}, allEntries = true)
    public ConsultationTypeResponse createConsultationType(ConsultationTypeRequest request) {
        ConsultationType type = new ConsultationType();
        type.setName(request.getName());
        type.setFee(request.getFee());
        type.setDurationMinutes(request.getDurationMinutes());
        type.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        
        type = consultationTypeRepository.save(type);
        log.info("Created new consultation type: {}", type.getName());
        return mapToConsultationTypeResponse(type);
    }

    @Transactional
    @CacheEvict(value = {"consultation_types", "consultation_type"}, allEntries = true)
    public ConsultationTypeResponse updateConsultationType(Long id, ConsultationTypeRequest request) {
        ConsultationType type = consultationTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Consultation type not found"));

        type.setName(request.getName());
        type.setFee(request.getFee());
        type.setDurationMinutes(request.getDurationMinutes());
        if (request.getIsActive() != null) {
            type.setIsActive(request.getIsActive());
        }

        type = consultationTypeRepository.save(type);
        log.info("Updated consultation type with id: {}", id);
        return mapToConsultationTypeResponse(type);
    }

    @Transactional
    @CacheEvict(value = {"consultation_types", "consultation_type"}, allEntries = true)
    public ConsultationTypeResponse updateStatus(Long id, ConsultationStatusRequest request) {
        ConsultationType type = consultationTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Consultation type not found"));

        type.setIsActive(Boolean.parseBoolean(request.getIsActive()));
        consultationTypeRepository.save(type);
        return mapToConsultationTypeResponse(type);
    }

    public ConsultationTypeResponse mapToConsultationTypeResponse(ConsultationType consultationType) {
        ConsultationTypeResponse consultationTypeResponse = new ConsultationTypeResponse();
        consultationTypeResponse.setId(consultationType.getId().toString());
        consultationTypeResponse.setName(consultationType.getName());
        consultationTypeResponse.setFee(consultationType.getFee().toString());
        consultationTypeResponse.setDurationMinutes(consultationType.getDurationMinutes().toString());
        consultationTypeResponse.setIsActive(consultationType.getIsActive().toString());
        consultationTypeResponse.setUpdatedAt(DateHelper.format(consultationType.getUpdatedAt()));
        consultationTypeResponse.setCreatedAt(DateHelper.format(consultationType.getCreatedAt()));
        return consultationTypeResponse;
    }
}