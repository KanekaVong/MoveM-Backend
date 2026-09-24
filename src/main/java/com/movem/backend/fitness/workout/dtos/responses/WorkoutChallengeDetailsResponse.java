package com.movem.backend.fitness.workout.dtos.responses;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class WorkoutChallengeDetailsResponse {

    private String type;

    private Integer id;

    private Integer participantId;

    private String name;

    private BigDecimal targetValue;

    private String targetUnit;
}