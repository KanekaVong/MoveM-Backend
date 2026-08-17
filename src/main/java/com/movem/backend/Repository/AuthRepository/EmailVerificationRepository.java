package com.movem.backend.Repository.AuthRepository;

import com.movem.backend.Entity.Auth.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findTopByUserIdOrderByCreatedAtDesc(Integer userId);
}