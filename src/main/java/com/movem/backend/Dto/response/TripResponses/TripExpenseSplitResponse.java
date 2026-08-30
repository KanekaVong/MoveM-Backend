package com.movem.backend.Dto.response.TripResponses;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripExpenseSplitResponse {

    private Integer id;

    private Integer userId;

    private String username;

    private BigDecimal amountOwed;

    private Boolean isSettled;

    private LocalDateTime settledAt;
}
