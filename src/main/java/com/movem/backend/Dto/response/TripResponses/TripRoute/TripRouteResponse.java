package com.movem.backend.Dto.response.TripResponses.TripRoute;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class TripRouteResponse {

    private String tripActivityId;
    private String destination;

    private BigDecimal totalDistanceKm;
    private Integer estimatedTravelTimeMinutes;

    private String encodedPolyline;

    private List<TripRouteStopResponse> stops;

    private List<RouteSegmentResponse> segments;
}