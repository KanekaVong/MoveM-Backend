package com.movem.backend.Dto.response.FitnessResponse.Achievement;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserAchievementResponse {

    private Integer achievementId;
    private String name;
    private String description;
    private String icon;
    private String conditionType;
    private java.math.BigDecimal conditionValue;
    private LocalDateTime earnedAt;
}