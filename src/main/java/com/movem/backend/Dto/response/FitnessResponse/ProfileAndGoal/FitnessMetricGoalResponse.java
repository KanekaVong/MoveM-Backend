package com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal;

import com.movem.backend.model.enums.Fitness.FitnessGoalMetric;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FitnessMetricGoalResponse {

    private Integer id;

    private FitnessGoalMetric metricType;

    private BigDecimal target;

    private String unit;

    private String period;

    private BigDecimal current;

    private BigDecimal remaining;

    private BigDecimal progressPercent;

    private String status;
}