package com.movem.backend.fitness.achievement.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAchievementResponse {
     Integer achievementId;
     String name;
     String description;
     String icon;
     String conditionType;
     BigDecimal conditionValue;
     LocalDateTime earnedAt;
     boolean notified;
}