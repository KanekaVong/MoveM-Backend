package com.movem.backend.Dto.request.FitnessRequest.Workout;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FinishWorkoutRequest {
    @NotNull
    @Min(0)
    private Integer durationSeconds;
    @NotNull
    @Min(0)
    private Integer steps;
    @DecimalMin("0.0")
    private BigDecimal distance;
}