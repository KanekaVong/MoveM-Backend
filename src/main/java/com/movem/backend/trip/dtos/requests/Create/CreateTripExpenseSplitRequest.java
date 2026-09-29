package com.movem.backend.trip.dtos.requests.Create;

import com.movem.backend.commons.enums.Trip.TripSplitMode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CreateTripExpenseSplitRequest {

    private TripSplitMode splitMode;

    private List<ExpenseSplitEntry> customSplits;

    @Getter
    @Setter
    public static class ExpenseSplitEntry {
        private Integer userId;
        private BigDecimal amount;
    }
}