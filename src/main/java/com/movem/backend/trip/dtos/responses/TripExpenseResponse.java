package com.movem.backend.trip.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripExpenseResponse {
     Integer id;
     Integer budgetId;
     String category;
     Integer payerId;
     String payerName;
     BigDecimal amount;
     String description;
     LocalDateTime expenseDate;
     List<TripExpenseSplitResponse> splits;
}
