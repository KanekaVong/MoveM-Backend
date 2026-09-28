package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.commons.enums.Fitness.WorkoutLevel;
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
public class SoloChallengeResponse {
     Integer id;
     String name;
     WorkoutType type;
     WorkoutLevel workoutLevel;
     BigDecimal targetValue;
     ChallengeTargetUnit targetUnit;
     BigDecimal calories;
     String description;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
}