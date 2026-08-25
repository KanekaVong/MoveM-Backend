package com.movem.backend.Dto.request.TripRequest.Update;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTripBudgetRequest {

    @NotBlank
    private String category;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal allocatedAmount;
}
