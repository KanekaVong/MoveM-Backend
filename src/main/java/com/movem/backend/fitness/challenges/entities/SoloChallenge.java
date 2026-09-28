package com.movem.backend.fitness.challenges.entities;

import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.commons.enums.Fitness.WorkoutLevel;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "solo_challenge_catalog")
@Getter
@Setter
public class SoloChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false, length = 150)
    String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    WorkoutType workoutType;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_level", nullable = false)
    WorkoutLevel workoutLevel;

    @Column(name = "target_value", nullable = false, precision = 10, scale = 2)
    BigDecimal targetValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_unit", nullable = false)
    ChallengeTargetUnit targetUnit;

    @Column(name = "calories")
    BigDecimal calories;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}