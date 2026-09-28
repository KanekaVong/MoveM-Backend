package com.movem.backend.fitness.challenges.entities;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Fitness.ChallengeSource;
import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.commons.enums.Fitness.FitnessChallengeStatus;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_fitness_challenge")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupFitnessChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false, unique = true)
    Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    FitnessClub fitnessClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    GroupChallengeCatalog catalog;

    @Column(nullable = false, length = 150)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    WorkoutType workoutType;

    @Column(name = "target_value", nullable = false, precision = 10, scale = 2)
    BigDecimal targetValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_unit", nullable = false)
    ChallengeTargetUnit targetUnit;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_source", nullable = false)
    ChallengeSource challengeSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    User createdBy;

    @Column(name = "start_at", nullable = false)
    LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    FitnessChallengeStatus status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}