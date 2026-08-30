package com.movem.backend.Dto.response.FitnessResponse.Social;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SocialWorkoutResponse {

    private Integer sessionId;
    private Integer userId;
    private String username;
    private String workoutType;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;
    private Integer durationSeconds;
    private BigDecimal distance;
    private BigDecimal averagePace;
    private BigDecimal averageSpeed;
    private BigDecimal caloriesBurned;
    private Integer steps;
}