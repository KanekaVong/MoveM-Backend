package com.movem.backend.Dto.response.FitnessResponse.Challenge;

import com.movem.backend.model.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.model.enums.Fitness.WorkoutLevel;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class SoloChallengeResponse {

    private Integer id;

    private String name;

    private WorkoutType type;

    private WorkoutLevel workoutLevel;

    private BigDecimal targetValue;

    private ChallengeTargetUnit targetUnit;

    private BigDecimal calories;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}