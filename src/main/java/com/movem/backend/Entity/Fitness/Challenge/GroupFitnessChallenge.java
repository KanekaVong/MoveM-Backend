package com.movem.backend.Entity.Fitness.Challenge;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Fitness.ChallengeSource;
import com.movem.backend.model.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.model.enums.Fitness.FitnessChallengeStatus;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_fitness_challenge")
@Getter
@Setter
public class GroupFitnessChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "activity_id",
            nullable = false,
            unique = true
    )
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "club_id",
            nullable = false
    )
    private FitnessClub fitnessClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id")
    private GroupChallengeCatalog catalog;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    private WorkoutType workoutType;

    @Column(
            name = "target_value",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal targetValue;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "target_unit",
            nullable = false
    )
    private ChallengeTargetUnit targetUnit;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "challenge_source",
            nullable = false
    )
    private ChallengeSource challengeSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            nullable = false
    )
    private User createdBy;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FitnessChallengeStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}