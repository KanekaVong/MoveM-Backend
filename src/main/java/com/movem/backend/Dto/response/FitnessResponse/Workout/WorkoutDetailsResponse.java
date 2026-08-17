package com.movem.backend.Dto.response.FitnessResponse.Workout;

import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class WorkoutDetailsResponse {

    private Integer id;

    private WorkoutType workoutType;

    private FitnessWorkoutStatus status;

    /*
     * Basic timing
     */
    private LocalDateTime startedAt;
    private LocalDateTime finishedAt;

    private Integer durationSeconds;
    private Integer totalPausedSeconds;

    /*
     * Workout metrics
     */
    private Integer steps;
    private BigDecimal distance;
    private BigDecimal caloriesBurned;
    private BigDecimal averagePace;

    /*
     * Calculated performance
     */
    private BigDecimal averageSpeed;
    private BigDecimal caloriesPerMinute;

    /*
     * Challenge information
     */
    private Integer soloChallengeId;
    private Integer groupChallengeId;
    private Integer groupParticipantId;

    private String challengeName;

    private BigDecimal challengeTargetValue;
    private String challengeTargetUnit;

    /*
     * User cumulative statistics
     */
    private Integer totalCompletedWorkouts;
    private BigDecimal totalDistance;
    private BigDecimal totalCaloriesBurned;
    private Long totalWorkoutSeconds;

    /*
     * Previous workout comparison
     */
    private Integer previousWorkoutId;
    private BigDecimal distanceChange;
    private BigDecimal calorieChange;
}