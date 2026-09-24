package com.movem.backend.fitness.workout.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class WorkoutHistoryResponse {

    private Integer id;

    private WorkoutType workoutType;

    private FitnessWorkoutStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private Integer durationSeconds;

    private BigDecimal distance;

    private BigDecimal caloriesBurned;
}