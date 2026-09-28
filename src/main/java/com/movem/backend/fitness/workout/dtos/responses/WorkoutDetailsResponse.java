package com.movem.backend.fitness.workout.dtos.responses;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
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
public class WorkoutDetailsResponse {
     Integer sessionId;

     WorkoutType workoutType;

     FitnessWorkoutStatus status;

     LocalDateTime startedAt;
     LocalDateTime finishedAt;

     Integer durationSeconds;
     Integer totalPausedSeconds;

     Integer steps;
     BigDecimal distance;
     BigDecimal caloriesBurned;
     String averagePace;
     BigDecimal averageSpeed;
     BigDecimal caloriesPerMinute;

     WorkoutChallengeDetailsResponse challenge;

     Integer totalCompletedWorkouts;
     BigDecimal totalDistance;
     BigDecimal totalCaloriesBurned;
     Long totalWorkoutSeconds;

     List<AttachmentResponse> attachments;

}