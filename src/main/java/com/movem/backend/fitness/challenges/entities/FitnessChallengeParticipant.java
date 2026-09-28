package com.movem.backend.fitness.challenges.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Fitness.FitnessChallengeParticipantStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_challenge_participant", uniqueConstraints = {@UniqueConstraint(name = "uk_challenge_participant", columnNames = {"challenge_id", "user_id"})})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessChallengeParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    GroupFitnessChallenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    FitnessChallengeParticipantStatus status;

    @Column(name = "joined_at", nullable = false)
    LocalDateTime joinedAt;

    @Column(name = "completed_at")
    LocalDateTime completedAt;


}
