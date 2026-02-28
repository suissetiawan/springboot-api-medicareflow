package com.dibimbing.medicareflow.entity;

import com.dibimbing.medicareflow.entity.base.BaseUuidEntity;
import com.dibimbing.medicareflow.enums.Role;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_account")
public class UserAccount extends BaseUuidEntity {
    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "userAccount")
    private Patient patient;

    @OneToOne(mappedBy = "userAccount")
    private Doctor doctor;
}
