package com.movem.backend.Dto.request.TripRequest.Create;

import com.movem.backend.model.enums.Trip.TripSplitMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTripExpenseRequest {

    @NotNull
    private Integer budgetId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;

    private String description;

    private LocalDateTime expenseDate;

    // Who paid — defaults to the caller if omitted. Must be a member of the trip.
    private Integer payerId;

    @NotNull
    private TripSplitMode splitMode = TripSplitMode.EQUAL;

    // Required only when splitMode = CUSTOM; amounts must sum to `amount`.
    @Valid
    private List<ExpenseSplitEntry> customSplits;

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
