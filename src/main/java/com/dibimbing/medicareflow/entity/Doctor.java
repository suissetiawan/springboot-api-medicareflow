package com.dibimbing.medicareflow.entity;

import com.dibimbing.medicareflow.entity.base.BaseUuidEntity;
import com.dibimbing.medicareflow.enums.DoctorStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
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
}
