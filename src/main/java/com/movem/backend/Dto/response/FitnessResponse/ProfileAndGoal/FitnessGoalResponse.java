package com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal;

import com.movem.backend.model.enums.Fitness.FitnessGoalMetric;
import com.movem.backend.model.enums.Fitness.GoalType;
import com.movem.backend.model.enums.Fitness.WorkoutLevel;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessGoalResponse {

    private Integer id;

    private Integer userId;

    private GoalType goalType;

    private FitnessGoalMetric metricType;

    private BigDecimal targetWeight;

    private LocalDate targetTimeline;

    private WorkoutLevel workoutLevel;

    private BigDecimal estimatedWeightChange;

    private BigDecimal estimatedDailyDeficit;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
