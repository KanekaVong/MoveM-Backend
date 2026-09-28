package com.movem.backend.fitness.profileandgoal.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessProfileResponse {
    Integer userId;
    BigDecimal height;
    BigDecimal weight;
    BigDecimal bmi;
    FitnessGoalResponse fitnessGoal;
    LocalDateTime updatedAt;
}