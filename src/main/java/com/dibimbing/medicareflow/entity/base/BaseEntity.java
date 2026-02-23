package com.dibimbing.medicareflow.entity.base;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    private LocalDateTime deleted_at;

    @CreationTimestamp()
    @Column(updatable = false)
    private LocalDateTime created_at;
    @UpdateTimestamp
    private LocalDateTime updated_at;
}
