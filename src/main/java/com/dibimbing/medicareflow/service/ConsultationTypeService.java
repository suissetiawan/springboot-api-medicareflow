package com.dibimbing.medicareflow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<ConsultationTypeResponse> getAllConsultationTypes() {
        List<ConsultationType> type = consultationTypeRepository.findAll();
        return type.stream().map(this::mapToConsultationTypeResponse).collect(Collectors.toList());
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