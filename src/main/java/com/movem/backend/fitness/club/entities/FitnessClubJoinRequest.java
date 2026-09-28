package com.movem.backend.fitness.club.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_club_join_requests", indexes = {
                @Index(name = "idx_fitness_club_request_club", columnList = "club_id"),
                @Index(name = "idx_fitness_club_request_user", columnList = "requester_id"),
                @Index(name = "idx_fitness_club_request_status", columnList = "status")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessClubJoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    FitnessClub fitnessClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    JoinRequestStatus status;

    @Column(name = "requested_at", nullable = false)
    LocalDateTime requestedAt;

    @Column(name = "responded_at")
    LocalDateTime respondedAt;
}