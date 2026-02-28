package com.dibimbing.medicareflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.ConsultationType;

@Repository
public interface ConsultationTypeRepository extends JpaRepository<ConsultationType, Long> {
    Optional<ConsultationType> findById(Long id);
}
