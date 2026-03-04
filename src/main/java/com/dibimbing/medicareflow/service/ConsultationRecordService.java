package com.dibimbing.medicareflow.service;

import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.request.ConsultationRecordRequest;
import com.dibimbing.medicareflow.dto.response.ConsultationRecordResponse;
import com.dibimbing.medicareflow.entity.Appointment;
import com.dibimbing.medicareflow.entity.ConsultationRecord;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.enums.AppointmentStatus;
import com.dibimbing.medicareflow.exception.ConflictException;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.SecurityHelper;
import com.dibimbing.medicareflow.repository.AppointmentRepository;
import com.dibimbing.medicareflow.repository.ConsultationRecordRepository;
import com.dibimbing.medicareflow.repository.DoctorRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationRecordService {

    private final ConsultationRecordRepository consultationRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional
    public ConsultationRecordResponse createRecord(Long appointmentId, ConsultationRecordRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if(consultationRecordRepository.findByAppointmentId(appointmentId).isPresent()) {
            throw new ConflictException("Consultation record already exists for this appointment");
        }

        String currentUsername = SecurityHelper.getCurrentUsername();
        var userAccount = userAccountRepository.findByUsername(currentUsername)
               .orElseThrow(() -> new NotFoundException("User not found"));

        if (!userAccount.getRole().name().equals("DOCTOR")) {
            throw new ConflictException("Only doctors can create consultation records");
        }

        Doctor doctor = doctorRepository.findByUserAccountId(userAccount.getId())
                 .orElseThrow(() -> new NotFoundException("Doctor profile not found"));

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
             throw new ConflictException("You are not authorized to create a record for this appointment");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED || appointment.getStatus() == AppointmentStatus.NO_SHOW) {
             throw new ConflictException("Cannot create record for cancelled or no-show appointment");
        }

        ConsultationRecord record = new ConsultationRecord();
        record.setAppointment(appointment);
        record.setSummary(request.getSummary());
        record.setRecommendation(request.getRecommendation());
        record.setFollowUpDate(request.getFollowUpDate());

        record = consultationRecordRepository.save(record);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return mapToResponse(record);
    }

    public ConsultationRecordResponse getRecordByAppointmentId(Long appointmentId) {
        ConsultationRecord record = consultationRecordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new NotFoundException("Consultation record not found"));

        String currentUsername = SecurityHelper.getCurrentUsername();
        var userAccount = userAccountRepository.findByUsername(currentUsername)
               .orElseThrow(() -> new NotFoundException("User not found"));

        // Basic authorization: Only the patient or the doctor of the appointment can view it. Admin could be added.
        Appointment appointment = record.getAppointment();
        if (userAccount.getRole().name().equals("PATIENT")) {
            if (!appointment.getPatient().getUserAccount().getId().equals(userAccount.getId())) {
                throw new ConflictException("You are not authorized to view this record");
            }
        } else if (userAccount.getRole().name().equals("DOCTOR")) {
            if (!appointment.getDoctor().getUserAccount().getId().equals(userAccount.getId())) {
                throw new ConflictException("You are not authorized to view this record");
            }
        }

        return mapToResponse(record);
    }

    private ConsultationRecordResponse mapToResponse(ConsultationRecord record) {
        return ConsultationRecordResponse.builder()
                .id(record.getId())
                .appointmentId(record.getAppointment().getId())
                .summary(record.getSummary())
                .recommendation(record.getRecommendation())
                .followUpDate(record.getFollowUpDate() != null ? record.getFollowUpDate().toString() : null)
                .build();
    }
}
