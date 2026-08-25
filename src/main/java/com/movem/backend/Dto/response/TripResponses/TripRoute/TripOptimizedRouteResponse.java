package com.movem.backend.Dto.response.TripResponses.TripRoute;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class TripOptimizedRouteResponse {

    private String tripActivityId;
    private String destination;

    private BigDecimal originalDistanceKm;
    private BigDecimal optimizedDistanceKm;

    private Integer originalTravelTimeMinutes;
    private Integer optimizedTravelTimeMinutes;

    private List<TripOptimizedStopResponse> optimizedStops;
}