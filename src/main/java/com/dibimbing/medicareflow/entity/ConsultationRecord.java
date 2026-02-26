package com.dibimbing.medicareflow.entity;

import java.time.LocalDate;

import com.dibimbing.medicareflow.entity.base.BaseUuidEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "consultation_record")
public class ConsultationRecord extends BaseUuidEntity {

    private String summary;
    private String recommendation;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
}
