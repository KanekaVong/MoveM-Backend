package com.movem.backend.trip.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripSummaryResponse {

    private String activityId;

    private String activityName;

    private String destination;

    private String locationName;

    private LocalDateTime startActivity;

    private LocalDateTime deadline;

    private ActivityStatus status;

    private Integer memberCount;

    private BigDecimal totalAllocatedBudget;

    private BigDecimal totalSpent;

}