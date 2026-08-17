package com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateFitnessProfileRequest {

    @NotNull(message = "Height is required.")
    @DecimalMin(value = "1.00", message = "Height must be greater than 0.")
    private BigDecimal height;

    @NotNull(message = "Weight is required.")
    @DecimalMin(value = "1.00", message = "Weight must be greater than 0.")
    private BigDecimal weight;
}