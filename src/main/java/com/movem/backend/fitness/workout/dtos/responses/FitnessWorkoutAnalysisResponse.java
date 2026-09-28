package com.movem.backend.fitness.workout.dtos.responses;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessWorkoutAnalysisResponse {
     Integer id;
     Integer sessionId;
     String exercise;
     Integer reps;
     Integer validReps;
     Integer invalidReps;
     Integer formScore;
     List<String> feedback;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
}