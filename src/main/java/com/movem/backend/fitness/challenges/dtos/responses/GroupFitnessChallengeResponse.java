package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.ChallengeSource;
import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.commons.enums.Fitness.FitnessChallengeStatus;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupFitnessChallengeResponse {
     Integer id;
     Integer clubId;
     Integer createdBy;
     String name;
     WorkoutType workoutType;
     BigDecimal targetValue;
     ChallengeTargetUnit targetUnit;
     String description;
     LocalDateTime startAt;
     LocalDateTime endAt;
     FitnessChallengeStatus status;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
     Integer catalogId;
     String imageUrl;
     ChallengeSource challengeSource;
}