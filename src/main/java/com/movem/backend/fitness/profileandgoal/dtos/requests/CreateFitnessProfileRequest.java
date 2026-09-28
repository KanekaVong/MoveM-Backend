package com.movem.backend.fitness.profileandgoal.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFitnessProfileRequest {

    @NotNull(message = "Height is required.")
    @DecimalMin(value = "1.00", message = "Height must be greater than 0.")
    BigDecimal height;

    @NotNull(message = "Weight is required.")
    @DecimalMin(value = "1.00", message = "Weight must be greater than 0.")
    BigDecimal weight;
}