package com.movem.backend.fitness.workout.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SharedWorkoutFeedResponse {

     Integer sessionId;
     Integer userId;
     String username;
     String profilePicture;

     String workoutType;
     String trackingMode;

     String shareDescription;

     BigDecimal distance;
     Integer steps;
     Integer durationSeconds;
     BigDecimal caloriesBurned;

     LocalDateTime finishedAt;
}