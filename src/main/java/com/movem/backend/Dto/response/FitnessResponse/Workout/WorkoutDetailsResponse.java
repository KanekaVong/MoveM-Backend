package com.movem.backend.Dto.response.FitnessResponse.Workout;

import com.movem.backend.Dto.response.Attachment.AttachmentResponse;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class WorkoutDetailsResponse {

    private Integer sessionId;

    private WorkoutType workoutType;

    private FitnessWorkoutStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private Integer durationSeconds;
    private Integer totalPausedSeconds;

    private Integer steps;
    private BigDecimal distance;
    private BigDecimal caloriesBurned;
    private String averagePace;
    private BigDecimal averageSpeed;
    private BigDecimal caloriesPerMinute;

    private WorkoutChallengeDetailsResponse challenge;

    private Integer totalCompletedWorkouts;
    private BigDecimal totalDistance;
    private BigDecimal totalCaloriesBurned;
    private Long totalWorkoutSeconds;

    private List<AttachmentResponse> attachments;

}