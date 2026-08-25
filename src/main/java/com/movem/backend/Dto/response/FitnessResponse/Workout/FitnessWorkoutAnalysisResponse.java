package com.movem.backend.Dto.response.FitnessResponse.Workout;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FitnessWorkoutAnalysisResponse {

    private Integer id;
    private Integer sessionId;
    private String exercise;
    private Integer reps;
    private Integer validReps;
    private Integer invalidReps;
    private Integer formScore;
    private List<String> feedback;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}