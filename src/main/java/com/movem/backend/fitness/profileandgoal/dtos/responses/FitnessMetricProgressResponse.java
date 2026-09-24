package com.movem.backend.fitness.profileandgoal.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessGoalMetric;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FitnessMetricProgressResponse {

    private FitnessGoalMetric metricType;

    private BigDecimal current;

    private BigDecimal target;

    private BigDecimal remaining;

    private BigDecimal progressPercent;

    private String unit;

    private String period;

    private boolean completed;
}