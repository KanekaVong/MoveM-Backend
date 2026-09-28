package com.movem.backend.fitness.profileandgoal.dtos.responses;

import com.movem.backend.commons.enums.Fitness.GoalType;
import com.movem.backend.commons.enums.Fitness.WorkoutLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessGoalResponse {
     Integer id;
     Integer userId;
     GoalType goalType;
     BigDecimal targetWeight;
     LocalDate targetTimeline;
     WorkoutLevel workoutLevel;
     BigDecimal estimatedWeightChange;
     BigDecimal estimatedDailyDeficit;
     String status;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;

}
