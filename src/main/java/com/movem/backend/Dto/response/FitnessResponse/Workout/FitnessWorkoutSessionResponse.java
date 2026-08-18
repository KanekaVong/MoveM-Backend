package com.movem.backend.Dto.response.FitnessResponse.Workout;

import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessWorkoutSessionResponse {

    private Integer sessionId;

    private Integer userId;

    private Integer soloChallengeId;

    private Integer groupChallengeParticipantId;

    private WorkoutType workoutType;

    private FitnessWorkoutStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private Integer durationSeconds;

    private Integer steps;

    private BigDecimal distance;

    private BigDecimal caloriesBurned;

    private String averagePace;}