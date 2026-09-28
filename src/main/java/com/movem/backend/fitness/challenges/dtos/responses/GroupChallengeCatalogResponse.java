package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
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
public class GroupChallengeCatalogResponse {
     Integer id;
     String name;
     WorkoutType workoutType;
     BigDecimal targetValue;
     ChallengeTargetUnit targetUnit;
     String description;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
}