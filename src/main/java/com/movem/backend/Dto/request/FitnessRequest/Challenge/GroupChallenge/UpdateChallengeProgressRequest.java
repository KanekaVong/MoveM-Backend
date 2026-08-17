package com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateChallengeProgressRequest {

    @NotNull(message = "Current value is required.")
    @DecimalMin(
            value = "0.0",
            message = "Current value cannot be negative."
    )
    private BigDecimal currentValue;
}
