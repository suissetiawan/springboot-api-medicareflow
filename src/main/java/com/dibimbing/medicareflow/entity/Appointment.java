package com.dibimbing.medicareflow.entity;

import java.time.LocalDateTime;

import com.dibimbing.medicareflow.entity.base.BaseUuidEntity;
import com.dibimbing.medicareflow.enums.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "appointment")
public class Appointment extends BaseUuidEntity {

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "booked_at", nullable = false)
    private LocalDateTime bookedAt;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "consultation_type_id", nullable = false)
    private ConsultationType service;

    @ManyToOne
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;
}
