package com.movem.backend.shared.analysis.statistics.dtos.responses;

import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessMetricProgressResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)public class FitnessStatisticsResponse {

    private long totalWorkouts;
    private long workoutsToday;
    private long workoutsThisWeek;

    private long totalSteps;
    private long stepsToday;
    private long stepsThisWeek;

    private BigDecimal totalDistance;
    private BigDecimal distanceToday;
    private BigDecimal distanceThisWeek;

    private BigDecimal caloriesToday;
    private BigDecimal caloriesThisWeek;
    private BigDecimal totalCalories;

    private List<FitnessMetricProgressResponse> metricGoals;

}