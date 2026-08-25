package com.movem.backend.Dto.response.FitnessResponse.Achievement;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AchievementResponse {

    private Integer achievementId;
    private String name;
    private String description;
    private String icon;
    private String category;
    private String conditionType;
    private BigDecimal conditionValue;

    private BigDecimal currentProgress;
    private BigDecimal progressPercentage;

    private boolean earned;
}