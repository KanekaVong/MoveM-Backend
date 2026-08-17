package com.movem.backend.Entity.Fitness.Challenge;

import com.movem.backend.model.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.model.enums.Fitness.WorkoutLevel;
import com.movem.backend.model.enums.Fitness.WorkoutType;
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
    private Integer id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    private WorkoutType workoutType;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_level", nullable = false)
    private WorkoutLevel workoutLevel;

    @Column(
            name = "target_value",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal targetValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_unit", nullable = false)
    private ChallengeTargetUnit targetUnit;

    @Column(name = "calories")
    private BigDecimal calories;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}