package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class GroupChallengeCatalogResponse {

    private Integer id;

    private String name;

    private WorkoutType workoutType;

    private BigDecimal targetValue;

    private ChallengeTargetUnit targetUnit;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}