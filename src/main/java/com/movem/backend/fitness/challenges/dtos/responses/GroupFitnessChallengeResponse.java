package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.ChallengeSource;
import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.commons.enums.Fitness.FitnessChallengeStatus;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class GroupFitnessChallengeResponse {
    private Integer id;
    private Integer clubId;
    private Integer createdBy;
    private String name;
    private WorkoutType workoutType;
    private BigDecimal targetValue;
    private ChallengeTargetUnit targetUnit;
    private String description;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private FitnessChallengeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer catalogId;
    private String imageUrl;
    private ChallengeSource challengeSource;
}