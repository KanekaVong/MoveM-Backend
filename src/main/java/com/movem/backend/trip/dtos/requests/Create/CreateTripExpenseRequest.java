package com.movem.backend.trip.dtos.requests.Create;

import com.movem.backend.commons.enums.Trip.TripSplitMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTripExpenseRequest {
    @NotNull
    Integer budgetId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    BigDecimal amount;

    String description;

    LocalDateTime expenseDate;

    Integer payerId;

    @NotNull
    TripSplitMode splitMode = TripSplitMode.EQUAL;

    @Valid
    List<ExpenseSplitEntry> customSplits;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseSplitEntry {
        @NotNull
        private Integer userId;

        @NotNull
        private BigDecimal amount;
    }
}
