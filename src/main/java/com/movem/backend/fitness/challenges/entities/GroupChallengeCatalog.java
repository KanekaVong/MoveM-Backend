package com.movem.backend.fitness.challenges.entities;

import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_challenge_catalog")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupChallengeCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

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

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}