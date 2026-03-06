package com.dibimbing.medicareflow.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dibimbing.medicareflow.entity.ConsultationType;

@Repository
public interface ConsultationTypeRepository extends JpaRepository<ConsultationType, Long> {
    Optional<ConsultationType> findById(Long id);

    @Query(value = "SELECT * FROM consultation_type WHERE id = :id AND deleted_at IS NOT NULL", nativeQuery = true)
    Optional<ConsultationType> findByDeletedId(Long id);

    @Query(value = "SELECT * FROM consultation_type WHERE deleted_at IS NOT NULL", 
           countQuery = "SELECT count(*) FROM consultation_type WHERE deleted_at IS NOT NULL", 
           nativeQuery = true)
    Page<ConsultationType> findAllDeleted(Pageable pageable);
}
