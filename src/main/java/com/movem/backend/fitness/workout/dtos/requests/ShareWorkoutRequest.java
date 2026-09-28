package com.movem.backend.fitness.workout.dtos.requests;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShareWorkoutRequest {
    Boolean shared;
    @Size(max = 500, message = "Share description cannot exceed 500 characters.")
    String description;
}