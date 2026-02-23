package com.dibimbing.medicareflow.repository.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {
    @Query("SELECT e FROM #{#entityName} e WHERE e.deleted_at IS NULL")
    List<T> findAll();

}
