package com.movem.backend.Dto.request.FitnessRequest.Workout;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FitnessWorkoutAnalysisRequest {

    @NotBlank
    private String exercise;

    @Min(0)
    private Integer reps;

    @Min(0)
    private Integer validReps;

    @Min(0)
    private Integer invalidReps;

    @Min(0)
    @Max(100)
    private Integer formScore;

    private List<String> feedback;
}