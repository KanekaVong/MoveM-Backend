package com.movem.backend.trip.dtos.responses;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripExpenseResponse {

    private Integer id;

    private Integer budgetId;

    private String category;

    private Integer payerId;

    private String payerName;

    private BigDecimal amount;

    private String description;

    private LocalDateTime expenseDate;

    private List<TripExpenseSplitResponse> splits;
}
