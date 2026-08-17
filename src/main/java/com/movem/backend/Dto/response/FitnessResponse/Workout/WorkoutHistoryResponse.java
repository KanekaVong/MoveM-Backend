package com.movem.backend.Dto.response.FitnessResponse.Workout;

import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.model.enums.Fitness.WorkoutType;
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