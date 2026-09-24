package com.movem.backend.fitness.workout.dtos.requests;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareWorkoutRequest {

    private Boolean shared;

    @Size(max = 500, message = "Share description cannot exceed 500 characters.")
    private String description;
}