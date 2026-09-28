package com.movem.backend.trip.dtos.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripBudgetResponse {
     Integer id;
     String category;
     BigDecimal allocatedAmount;
     BigDecimal spentAmount;
     BigDecimal remaining;
     BigDecimal perPersonShare;
}
