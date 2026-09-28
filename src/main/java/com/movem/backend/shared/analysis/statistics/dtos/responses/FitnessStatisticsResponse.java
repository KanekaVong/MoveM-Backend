package com.movem.backend.shared.analysis.statistics.dtos.responses;

import com.movem.backend.fitness.profileandgoal.dtos.responses.FitnessMetricProgressResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessStatisticsResponse {
     long totalWorkouts;
     long workoutsToday;
     long workoutsThisWeek;

     long totalSteps;
     long stepsToday;
     long stepsThisWeek;

     BigDecimal totalDistance;
     BigDecimal distanceToday;
     BigDecimal distanceThisWeek;

     BigDecimal caloriesToday;
     BigDecimal caloriesThisWeek;
     BigDecimal totalCalories;

     List<FitnessMetricProgressResponse> metricGoals;
}