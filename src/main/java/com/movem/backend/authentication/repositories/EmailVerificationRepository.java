package com.movem.backend.authentication.repositories;

import com.movem.backend.authentication.entities.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findTopByUserIdOrderByCreatedAtDesc(Integer userId);
}