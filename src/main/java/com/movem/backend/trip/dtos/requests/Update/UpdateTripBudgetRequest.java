package com.movem.backend.trip.dtos.requests.Update;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateTripBudgetRequest {
    Integer id;
    @NotBlank
    String category;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    BigDecimal allocatedAmount;
}
