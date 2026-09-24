package com.movem.backend.trip.dtos.responses;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripBudgetResponse {
    private Integer id;
    private String category;
    private BigDecimal allocatedAmount;
    private BigDecimal spentAmount;
    private BigDecimal remaining;
    private BigDecimal perPersonShare;
}
