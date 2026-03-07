package com.dibimbing.medicareflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.entity.ConsultationType;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.exception.ConflictException;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.repository.ConsultationTypeRepository;
import com.dibimbing.medicareflow.repository.DoctorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final ConsultationTypeRepository consultationTypeRepository;

    @Transactional
    public String addService(String username, Long consultationTypeId) {
        Doctor doctor = doctorRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));
        
        ConsultationType consultationType = consultationTypeRepository.findById(consultationTypeId)
                .orElseThrow(() -> new NotFoundException("Consultation type not found"));

        if (doctor.getServices().contains(consultationType)) {
            throw new ConflictException("Service already added");
        }

        doctor.getServices().add(consultationType);
        doctorRepository.save(doctor);
        return "Service added successfully";
    }

    @Transactional
    public String removeService(String username, Long consultationTypeId) {
        Doctor doctor = doctorRepository.findByUserAccountUsername(username)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        ConsultationType consultationType = consultationTypeRepository.findById(consultationTypeId)
                .orElseThrow(() -> new NotFoundException("Consultation type not found"));

        if (!doctor.getServices().contains(consultationType)) {
            throw new NotFoundException("Service not found");
        }

        doctor.getServices().remove(consultationType);
        doctorRepository.save(doctor);
        return "Service removed successfully";
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
