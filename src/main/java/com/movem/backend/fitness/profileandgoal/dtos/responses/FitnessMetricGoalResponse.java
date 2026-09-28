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
public class FitnessMetricGoalResponse {
    Integer id;
    FitnessGoalMetric metricType;
    BigDecimal target;
    String unit;
    String period;
    BigDecimal current;
    BigDecimal remaining;
    BigDecimal progressPercent;
    String status;
}