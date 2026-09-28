package com.movem.backend.fitness.achievement.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AchievementResponse {
     Integer achievementId;
     String name;
     String description;
     String icon;
     String category;
     String conditionType;
     BigDecimal conditionValue;
     BigDecimal currentProgress;
     BigDecimal progressPercentage;
     boolean earned;
}