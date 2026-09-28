package com.movem.backend.fitness.workout.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import com.movem.backend.fitness.achievement.dtos.responses.UserAchievementResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessWorkoutSessionResponse {
     Integer sessionId;
     Integer userId;
     Integer soloChallengeId;
     Integer groupChallengeParticipantId;
     WorkoutType workoutType;
     FitnessWorkoutStatus status;
     LocalDateTime startedAt;
     LocalDateTime finishedAt;
     Integer durationSeconds;
     Integer steps;
     BigDecimal distance;
     BigDecimal caloriesBurned;
     String averagePace;
     BigDecimal height;
     BigDecimal weight;
     BigDecimal bmi;
     List<UserAchievementResponse> currentAchievements;
}