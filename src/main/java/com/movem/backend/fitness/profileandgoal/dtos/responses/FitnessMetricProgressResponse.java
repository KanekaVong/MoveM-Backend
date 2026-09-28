package com.movem.backend.fitness.profileandgoal.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessGoalMetric;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessMetricProgressResponse {
    FitnessGoalMetric metricType;
    BigDecimal current;
    BigDecimal target;
    BigDecimal remaining;
    BigDecimal progressPercent;
    String unit;
    String period;
    boolean completed;
}