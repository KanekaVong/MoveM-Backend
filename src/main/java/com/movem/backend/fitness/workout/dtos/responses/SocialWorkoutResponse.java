package com.movem.backend.fitness.workout.dtos.responses;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialWorkoutResponse {
     Integer sessionId;
     Integer userId;
     String username;

     String workoutType;
     String status;

     LocalDateTime startedAt;
     LocalDateTime finishedAt;

     Integer durationSeconds;

     BigDecimal distance;
     BigDecimal averagePace;
     BigDecimal averageSpeed;

     BigDecimal caloriesBurned;
     Integer steps;

     List<AttachmentResponse> attachments;
     List<FitnessWorkoutRoutePointResponse> points;
}