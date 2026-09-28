package com.movem.backend.fitness.workout.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
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
public class WorkoutHistoryResponse {
     Integer id;
     WorkoutType workoutType;
     FitnessWorkoutStatus status;
     LocalDateTime startedAt;
     LocalDateTime finishedAt;
     Integer durationSeconds;
     BigDecimal distance;
     BigDecimal caloriesBurned;
}