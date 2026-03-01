package com.dibimbing.medicareflow.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dibimbing.medicareflow.dto.request.AppointmentRequest;
import com.dibimbing.medicareflow.dto.response.AppointmentResponse;
import com.dibimbing.medicareflow.entity.Appointment;
import com.dibimbing.medicareflow.entity.ConsultationType;
import com.dibimbing.medicareflow.entity.Doctor;
import com.dibimbing.medicareflow.entity.Patient;
import com.dibimbing.medicareflow.entity.TimeSlot;
import com.dibimbing.medicareflow.enums.AppointmentStatus;
import com.dibimbing.medicareflow.enums.SlotStatus;
import com.dibimbing.medicareflow.exception.ConflictException;
import com.dibimbing.medicareflow.exception.NotFoundException;
import com.dibimbing.medicareflow.helper.SecurityHelper;
import com.dibimbing.medicareflow.repository.AppointmentRepository;
import com.dibimbing.medicareflow.repository.ConsultationTypeRepository;
import com.dibimbing.medicareflow.repository.DoctorRepository;
import com.dibimbing.medicareflow.repository.PatientRepository;
import com.dibimbing.medicareflow.repository.TimeSlotRepository;
import com.dibimbing.medicareflow.repository.UserAccountRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ConsultationTypeRepository consultationTypeRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        String currentUsername = SecurityHelper.getCurrentUsername();
        var userOpt = userAccountRepository.findByUsername(currentUsername);
        
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        
        var userAccount = userOpt.get();
        if(!userAccount.getRole().name().equals("PATIENT")) {
            throw new ConflictException("Only patients can book appointments");
        }

        Patient patient = patientRepository.findByUserAccountId(userAccount.getId())
                .orElseThrow(() -> new NotFoundException("Patient profile not found"));

        TimeSlot timeSlot = timeSlotRepository.findById(request.getTimeSlotId())
                .orElseThrow(() -> new NotFoundException("Time slot not found"));

        if (timeSlot.getStatus() != SlotStatus.AVAILABLE) {
            throw new ConflictException("Time slot is not available");
        }

        ConsultationType consultationType = consultationTypeRepository.findById(request.getConsultationTypeId())
                .orElseThrow(() -> new NotFoundException("Consultation type not found"));

        Doctor doctor = timeSlot.getDoctor();
        if(!doctor.getServices().contains(consultationType)) {
             throw new ConflictException("Doctor does not offer the requested consultation type");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setService(consultationType);
        appointment.setTimeSlot(timeSlot);
        appointment.setBookedAt(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.PENDING);
        appointment.setReferenceNumber(generateReferenceNumber());

        appointment = appointmentRepository.save(appointment);

        timeSlot.setStatus(SlotStatus.BOOKED);
        timeSlotRepository.save(timeSlot);

        return mapToResponse(appointment);
    }

    public Page<AppointmentResponse> getMyAppointments(Pageable pageable) {
        String currentUsername = SecurityHelper.getCurrentUsername();
        var userAccount = userAccountRepository.findByUsername(currentUsername)
               .orElseThrow(() -> new NotFoundException("User not found"));

        if (userAccount.getRole().name().equals("PATIENT")) {
            Patient patient = patientRepository.findByUserAccountId(userAccount.getId())
                    .orElseThrow(() -> new NotFoundException("Patient profile not found"));
            return appointmentRepository.findByPatientId(patient.getId(), pageable).map(this::mapToResponse);
        } else if (userAccount.getRole().name().equals("DOCTOR")) {
            Doctor doctor = doctorRepository.findByUserAccountId(userAccount.getId())
                    .orElseThrow(() -> new NotFoundException("Doctor profile not found"));
            return appointmentRepository.findByDoctorId(doctor.getId(), pageable).map(this::mapToResponse);
        } else {
             throw new ConflictException("Users with this role do not have personal appointments");
        }
    }

    public Page<AppointmentResponse> getAllAppointments(Pageable pageable) {
        return appointmentRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));
                
        String currentUsername = SecurityHelper.getCurrentUsername();
        var userAccount = userAccountRepository.findByUsername(currentUsername)
               .orElseThrow(() -> new NotFoundException("User not found"));

        String role = userAccount.getRole().name();

        if (role.equals("PATIENT")) {
            Patient patient = patientRepository.findByUserAccountId(userAccount.getId()).get();
            if(!appointment.getPatient().getId().equals(patient.getId())) {
                throw new ConflictException("You are not authorized to update this appointment");
            }
            if(newStatus != AppointmentStatus.CANCELLED) {
                throw new ConflictException("Patients can only cancel their appointments");
            }
        } 
        
        if (role.equals("DOCTOR")) {
           Doctor doctor = doctorRepository.findByUserAccountId(userAccount.getId()).get();
           if(!appointment.getDoctor().getId().equals(doctor.getId())) {
               throw new ConflictException("You are not authorized to update this appointment");
           }
        }

        appointment.setStatus(newStatus);
        
        if (newStatus == AppointmentStatus.CANCELLED) {
            TimeSlot timeSlot = appointment.getTimeSlot();
            timeSlot.setStatus(SlotStatus.AVAILABLE);
            timeSlotRepository.save(timeSlot);
        }

        appointment = appointmentRepository.save(appointment);
        return mapToResponse(appointment);
    }

    private String generateReferenceNumber() {
        return "APT-" + System.currentTimeMillis();
    }

    private AppointmentResponse mapToResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .referenceNumber(appointment.getReferenceNumber())
                .doctorName(appointment.getDoctor().getName())
                .patientName(appointment.getPatient().getName())
                .consultationType(appointment.getService().getName())
                .status(appointment.getStatus().name())
                .appointmentDate(appointment.getTimeSlot().getSlotDate().toString())
                .startTime(appointment.getTimeSlot().getStartTime().toString())
                .endTime(appointment.getTimeSlot().getEndTime().toString())
                .bookedAt(appointment.getBookedAt().toString())
                .build();
    }
}
