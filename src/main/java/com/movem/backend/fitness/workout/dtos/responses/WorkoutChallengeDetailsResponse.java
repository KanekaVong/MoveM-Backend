package com.movem.backend.fitness.workout.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkoutChallengeDetailsResponse {
     String type;
     Integer id;
     Integer participantId;
     String name;
     BigDecimal targetValue;
     String targetUnit;
}