package com.movem.backend.fitness.profileandgoal.dtos.responses;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessProfileResponse {

    private Integer userId;

    private BigDecimal height;

    private BigDecimal weight;

    private BigDecimal bmi;

    private FitnessGoalResponse fitnessGoal;

    private LocalDateTime updatedAt;
}