package com.movem.backend.Dto.response.FitnessResponse.Workout;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FitnessWorkoutSummaryResponse {

    private Integer sessionId;
    private Integer userId;
    private String workoutType;
    private String trackingMode;
    private String status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Integer durationSeconds;

    private BigDecimal distance;
    private Integer steps;
    private BigDecimal caloriesBurned;

    private Integer reps;
    private Integer validReps;
    private Integer invalidReps;
    private Integer formScore;
    private List<String> feedback;
}