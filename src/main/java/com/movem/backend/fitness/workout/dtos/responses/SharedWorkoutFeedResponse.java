package com.movem.backend.fitness.workout.dtos.responses;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class SharedWorkoutFeedResponse {

    private Integer sessionId;
    private Integer userId;
    private String username;
    private String profilePicture;

    private String workoutType;
    private String trackingMode;

    private String shareDescription;

    private BigDecimal distance;
    private Integer steps;
    private Integer durationSeconds;
    private BigDecimal caloriesBurned;

    private LocalDateTime finishedAt;
}