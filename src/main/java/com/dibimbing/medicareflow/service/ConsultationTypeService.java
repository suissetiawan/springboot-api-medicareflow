package com.dibimbing.medicareflow.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.request.ConsultationTypeRequest;
import com.dibimbing.medicareflow.dto.response.ConsultationTypeResponse;
import com.dibimbing.medicareflow.entity.ConsultationType;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.DateHelper;
import com.dibimbing.medicareflow.repository.ConsultationTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationTypeService {

    private ConsultationTypeRepository consultationTypeRepository;

    public Page<ConsultationTypeResponse> getAllConsultationTypes(Pageable pageable) {
        Page<ConsultationType> type = consultationTypeRepository.findAll(pageable);
        return type.map(this::mapToConsultationTypeResponse);
    }

    public ConsultationTypeResponse getConsultationTypeById(Long id) {
        ConsultationType type = consultationTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Consultation type not found");
                    throw new NotFoundException("Consultation type not found");
                });

        return mapToConsultationTypeResponse(type);
    }

    public ConsultationTypeResponse updateStatus(Long id, ConsultationTypeRequest request) {
        ConsultationType type = consultationTypeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Consultation type not found");
                    throw new NotFoundException("Consultation type not found");
                });

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