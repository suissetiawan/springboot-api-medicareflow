package com.dibimbing.medicareflow.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dibimbing.medicareflow.entity.UserAccount;
import com.dibimbing.medicareflow.enums.Role;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    @Query("SELECT u FROM UserAccount u WHERE u.username = :username")
    Optional<UserAccount> findByUsername(String username);

    @Query("SELECT u FROM UserAccount u WHERE u.email = :email")
    Optional<UserAccount> findByEmail(String email);

    @Query("SELECT u FROM UserAccount u WHERE u.role = :role")
    List<UserAccount> findByRole(Role role);

    
    
}
