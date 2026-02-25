package com.dibimbing.medicareflow.entity;

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
@Table(name = "patient")
public class Patient extends BaseUuidEntity {
    private String name;
    private String phone;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;
}
