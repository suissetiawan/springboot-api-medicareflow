package com.dibimbing.medicareflow.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.request.ConsultationRecordRequest;
import com.dibimbing.medicareflow.dto.response.ConsultationRecordResponse;
import com.dibimbing.medicareflow.entity.Appointment;
import com.dibimbing.medicareflow.entity.ConsultationRecord;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.enums.AppointmentStatus;
import com.dibimbing.medicareflow.enums.Role;
import com.dibimbing.medicareflow.exception.ConflictException;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.SecurityHelper;
import com.dibimbing.medicareflow.repository.AppointmentRepository;
import com.dibimbing.medicareflow.repository.ConsultationRecordRepository;
import com.dibimbing.medicareflow.repository.DoctorRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.dibimbing.medicareflow.helper.RestPage;

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
    @CacheEvict(value = {"consultation_records", "consultation_record"}, allEntries = true)
    public ConsultationRecordResponse createRecord(Long appointmentId, ConsultationRecordRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));

        if(consultationRecordRepository.findByAppointmentId(appointmentId).isPresent()) {
            throw new ConflictException("Consultation record already exists for this appointment");
        }

        String currentUsername = SecurityHelper.getCurrentUsername();
        var userAccount = userAccountRepository.findByUsername(currentUsername)
               .orElseThrow(() -> new NotFoundException("User not found"));

        if (userAccount.getRole() != Role.DOCTOR) {
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

        return mapToResponse(record, userAccount.getRole());
    }

    @Cacheable(value = "consultation_record", key = "#appointmentId + '_' + #username")
    public ConsultationRecordResponse getRecordByAppointmentId(Long appointmentId, String username) {
        ConsultationRecord record = consultationRecordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new NotFoundException("Consultation record not found"));

        var userAccount = userAccountRepository.findByUsername(username)
               .orElseThrow(() -> new NotFoundException("User not found"));

        Appointment appointment = record.getAppointment();
        if (userAccount.getRole() == Role.PATIENT) {
            if (!appointment.getPatient().getUserAccount().getId().equals(userAccount.getId())) {
                throw new ConflictException("You are not authorized to view this record");
            }
        } else if (userAccount.getRole() == Role.DOCTOR) {
            if (!appointment.getDoctor().getUserAccount().getId().equals(userAccount.getId())) {
                throw new ConflictException("You are not authorized to view this record");
            }
        }

        return mapToResponse(record, userAccount.getRole());
    }

    @Cacheable(value = "consultation_records", key = "#username + '_' + #pageable.toString()")
    public Page<ConsultationRecordResponse> getMyRecords(String username, Pageable pageable) {
        var userAccount = userAccountRepository.findByUsername(username)
               .orElseThrow(() -> new NotFoundException("User not found"));

        Page<ConsultationRecord> records;
        if (userAccount.getRole() == Role.PATIENT) {
            records = consultationRecordRepository.findByAppointmentPatientUserAccountId(userAccount.getId(), pageable);
        } else if (userAccount.getRole() == Role.DOCTOR) {
            records = consultationRecordRepository.findByAppointmentDoctorUserAccountId(userAccount.getId(), pageable);
        } else {
            throw new ConflictException("Admins do not have personal consultation records");
        }
        
        return new RestPage<>(records.getContent().stream().map(record -> mapToResponse(record, userAccount.getRole())).toList(), pageable, records.getTotalElements());
    }

    @Cacheable(value = "consultation_records", key = "'all_' + (#doctorUsername != null ? #doctorUsername : 'ALL') + '_' + (#patientUsername != null ? #patientUsername : 'ALL') + '_' + #pageable.toString()")
    public Page<ConsultationRecordResponse> getAllRecords(String doctorUsername, String patientUsername, Pageable pageable) {
        String currentUsername = SecurityHelper.getCurrentUsername();
        var userAccount = userAccountRepository.findByUsername(currentUsername)
               .orElseThrow(() -> new NotFoundException("User not found"));

        if (userAccount.getRole() != Role.ADMIN) {
             throw new ConflictException("Only admins can view all consultation records");
        }

        Page<ConsultationRecord> records;
        if (doctorUsername != null) {
            records = consultationRecordRepository.findByAppointmentDoctorUserAccountUsername(doctorUsername, pageable);
        } else if (patientUsername != null) {
            records = consultationRecordRepository.findByAppointmentPatientUserAccountUsername(patientUsername, pageable);
        } else {
            records = consultationRecordRepository.findAll(pageable);
        }
        
        return new RestPage<>(records.getContent().stream().map(record -> mapToResponse(record, Role.ADMIN)).toList(), pageable, records.getTotalElements());
    }

    private ConsultationRecordResponse mapToResponse(ConsultationRecord record, Role requesterRole) {
        Long appointmentId = record.getAppointment() != null ? record.getAppointment().getId() : null;

        var builder = ConsultationRecordResponse.builder()
                .id(record.getId())
                .appointmentId(appointmentId)
                .summary(record.getSummary())
                .recommendation(record.getRecommendation())
                .followUpDate(record.getFollowUpDate() != null ? record.getFollowUpDate().toString() : null);

        if (record.getAppointment() != null) {
            String doctorUsername = record.getAppointment().getDoctor() != null && record.getAppointment().getDoctor().getUserAccount() != null ? record.getAppointment().getDoctor().getUserAccount().getUsername() : null;
            String patientUsername = record.getAppointment().getPatient() != null && record.getAppointment().getPatient().getUserAccount() != null ? record.getAppointment().getPatient().getUserAccount().getUsername() : null;

            if (requesterRole == Role.PATIENT) {
                builder.doctorUsername(doctorUsername);
            } else if (requesterRole == Role.DOCTOR) {
                builder.patientUsername(patientUsername);
            } else if (requesterRole == Role.ADMIN) {
                builder.doctorUsername(doctorUsername);
                builder.patientUsername(patientUsername);
            }
        }

        return builder.build();
    }
}
