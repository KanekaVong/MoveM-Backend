package com.movem.backend.fitness.workout.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessWorkoutSummaryResponse {
     Integer sessionId;
     Integer userId;
     String workoutType;
     String trackingMode;
     String status;

     LocalDateTime startedAt;
     LocalDateTime finishedAt;
     Integer durationSeconds;

     BigDecimal distance;
     Integer steps;
     BigDecimal caloriesBurned;

     Integer reps;
     Integer validReps;
     Integer invalidReps;
     Integer formScore;
     List<String> feedback;
}