package com.movem.backend.fitness.workout.dtos.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessWorkoutAnalysisRequest {
    @NotBlank
    String exercise;

    @Min(0)
    Integer reps;

    @Min(0)
    Integer validReps;

    @Min(0)
    Integer invalidReps;

    @Min(0) @Max(100)
    Integer formScore;
    List<String> feedback;
}