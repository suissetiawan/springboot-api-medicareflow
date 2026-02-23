package com.dibimbing.medicareflow.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;

import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.repository.base.BaseRepository;

public interface UserAccountRepository extends BaseRepository<UserAccount, UUID> {

    @Query("SELECT u FROM UserAccount u WHERE u.username = :username AND u.deleted_at IS NULL")
    Optional<UserAccount> findByUsername(String username);

    @Query("SELECT u FROM UserAccount u WHERE u.email = :email AND u.deleted_at IS NULL")
    Optional<UserAccount> findByEmail(String email);

}
