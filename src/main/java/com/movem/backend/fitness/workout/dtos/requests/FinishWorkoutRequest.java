package com.movem.backend.fitness.workout.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FinishWorkoutRequest {
    @NotNull @Min(0)
    Integer durationSeconds;
    @NotNull @Min(0)
    Integer steps;
    @DecimalMin("0.0")
    BigDecimal distance;
}