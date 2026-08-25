package com.movem.backend.Dto.response.TripResponses;

import com.movem.backend.model.enums.Activity.ActivityStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripResponse {

    private String activityId;
    private String activityName;
    private String description;
    private ActivityStatus status;
    private LocalDateTime startActivity;
    private LocalDateTime deadline;

    private String locationName;
    private String locationAddress;
    private BigDecimal lat;
    private BigDecimal lng;
    private String googlePlaceId;

    private String destination;
    private String flightNumber;
    private String hotelName;

    private List<TripStopResponse> stops;

    private Integer memberCount;

    private BigDecimal totalAllocatedBudget;
    private BigDecimal totalSpent;
    private BigDecimal perPersonShare;

}
