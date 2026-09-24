package com.movem.backend.fitness.workout.dtos.responses;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    private List<AttachmentResponse> attachments;
    private List<FitnessWorkoutRoutePointResponse> points;
}