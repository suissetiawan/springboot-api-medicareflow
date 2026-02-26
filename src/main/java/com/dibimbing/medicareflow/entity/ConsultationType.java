package com.dibimbing.medicareflow.entity;

import java.math.BigDecimal;
import java.util.List;

import com.dibimbing.medicareflow.entity.base.BaseLongEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "consultation_type")
public class ConsultationType extends BaseLongEntity {

    private String name;
    private BigDecimal fee;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToMany(mappedBy = "services", fetch = FetchType.LAZY)
    private List<Doctor> doctors;
}
