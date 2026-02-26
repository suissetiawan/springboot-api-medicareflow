package com.dibimbing.medicareflow.entity;

import java.util.List;

import com.dibimbing.medicareflow.entity.base.BaseUuidEntity;
import com.dibimbing.medicareflow.enums.DoctorStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "doctor")
public class Doctor extends BaseUuidEntity {

    private String name;
    private String specialization;

    @Enumerated(EnumType.STRING)
    private DoctorStatus status;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "doctor_service",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    private List<ConsultationType> services;
}
