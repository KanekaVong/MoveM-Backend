package com.movem.backend.Dto.response.TripResponses;

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
