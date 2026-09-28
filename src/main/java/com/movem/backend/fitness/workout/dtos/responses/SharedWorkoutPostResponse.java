package com.movem.backend.fitness.workout.dtos.responses;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SharedWorkoutPostResponse {

     Integer sessionId;
     Integer userId;
     String username;
     String profilePicture;
     String workoutType;
     String trackingMode;

     String shareDescription;

     BigDecimal distance;
     Integer steps;
     Integer durationSeconds;
     BigDecimal caloriesBurned;

     LocalDateTime finishedAt;

     long kudosCount;
     boolean myKudos;
     long commentCount;
     boolean myPost;

     List<AttachmentResponse> attachments;
     List<FitnessWorkoutRoutePointResponse> points;

}