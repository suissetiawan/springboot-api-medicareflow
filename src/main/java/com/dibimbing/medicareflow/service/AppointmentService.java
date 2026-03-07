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
import com.dibimbing.medicareflow.enums.Role;
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

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.dibimbing.medicareflow.helper.RestPage;
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
    @CacheEvict(value = {"appointments", "appointment"}, allEntries = true)
    public AppointmentResponse createAppointment(AppointmentRequest request) {
        String currentUsername = SecurityHelper.getCurrentUsername();
        var userOpt = userAccountRepository.findByUsername(currentUsername);
        
        if (userOpt.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        
        var userAccount = userOpt.get();
        if(userAccount.getRole() != Role.PATIENT) {
            throw new ConflictException("Only patients can book appointments");
        }

        Patient patient = patientRepository.findByUserAccountId(userAccount.getId())
                .or(() -> patientRepository.findById(userAccount.getId()))
                .orElseThrow(() -> new NotFoundException("Patient profile not found for user: " + currentUsername + " (ID: " + userAccount.getId() + ")"));

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

    @Cacheable(value = "appointments", key = "#username + '_' + #pageable.toString()")
    public Page<AppointmentResponse> getMyAppointments(String username, Pageable pageable) {
        var userAccount = userAccountRepository.findByUsername(username)
               .orElseThrow(() -> new NotFoundException("User not found"));

        if (userAccount.getRole() == Role.PATIENT) {
            Patient patient = patientRepository.findByUserAccountId(userAccount.getId())
                    .orElseThrow(() -> new NotFoundException("Patient profile not found"));
            
            Page<Appointment> apps = appointmentRepository.findByPatientId(patient.getId(), pageable);
            return new RestPage<>(apps.getContent().stream().map(this::mapToResponse).toList(), pageable, apps.getTotalElements());

        } else if (userAccount.getRole() == Role.DOCTOR) {
            Doctor doctor = doctorRepository.findByUserAccountId(userAccount.getId())
                    .orElseThrow(() -> new NotFoundException("Doctor profile not found"));

            Page<Appointment> apps = appointmentRepository.findByDoctorId(doctor.getId(), pageable);
            return new RestPage<>(apps.getContent().stream().map(this::mapToResponse).toList(), pageable, apps.getTotalElements());

        } else {
             throw new ConflictException("Users with this role do not have personal appointments");
        }
    }

    @Cacheable(value = "appointments", key = "'all_' + #pageable.toString()")
    public Page<AppointmentResponse> getAllAppointments(Pageable pageable) {
        Page<Appointment> apps = appointmentRepository.findAll(pageable);
        return new RestPage<>(apps.getContent().stream().map(this::mapToResponse).toList(), pageable, apps.getTotalElements());
    }

    @Transactional
    @CacheEvict(value = {"appointments", "appointment"}, allEntries = true)
    public AppointmentResponse updateAppointmentStatus(Long appointmentId, AppointmentStatus nextStatus) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new NotFoundException("Appointment not found"));
                
        AppointmentStatus currentStatus = appointment.getStatus();

        if (currentStatus == nextStatus) {
            return mapToResponse(appointment);
        }

        // --- STRICT STATE MACHINE ---
        // 1. Final States (CANCELLED, COMPLETED, NO_SHOW)
        if (currentStatus == AppointmentStatus.CANCELLED || 
            currentStatus == AppointmentStatus.COMPLETED || 
            currentStatus == AppointmentStatus.NO_SHOW) {
            throw new ConflictException("Appointment is in a final state (" + currentStatus + ") and cannot be changed");
        }

        // 2. Transition Rules
        if (currentStatus == AppointmentStatus.PENDING) {
            if (nextStatus != AppointmentStatus.CONFIRMED && nextStatus != AppointmentStatus.CANCELLED) {
                throw new ConflictException("PENDING appointments can only transition to CONFIRMED or CANCELLED");
            }
        } else if (currentStatus == AppointmentStatus.CONFIRMED) {
            if (nextStatus == AppointmentStatus.PENDING) {
                throw new ConflictException("CONFIRMED appointments cannot transition back to PENDING");
            }
            // Allowed: CONFIRMED -> COMPLETED, NO_SHOW, CANCELLED
        }

        appointment.setStatus(nextStatus);
        
        if (nextStatus == AppointmentStatus.CANCELLED) {
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
        String doctorName = appointment.getDoctor() != null ? appointment.getDoctor().getName() : "Deleted Doctor";
        String patientName = appointment.getPatient() != null ? appointment.getPatient().getName() : "Deleted Patient";

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .referenceNumber(appointment.getReferenceNumber())
                .doctorName(doctorName)
                .patientName(patientName)
                .consultationType(appointment.getService().getName())
                .status(appointment.getStatus().name())
                .appointmentDate(appointment.getTimeSlot().getSlotDate().toString())
                .startTime(appointment.getTimeSlot().getStartTime().toString())
                .endTime(appointment.getTimeSlot().getEndTime().toString())
                .bookedAt(appointment.getBookedAt().toString())
                .build();
    }
}
