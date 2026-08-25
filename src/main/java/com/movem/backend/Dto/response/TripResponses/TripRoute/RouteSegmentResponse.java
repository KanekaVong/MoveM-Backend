package com.movem.backend.Dto.response.TripResponses.TripRoute;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class RouteSegmentResponse {

    private Integer sequenceOrder;

    private String from;
    private String to;

    private BigDecimal distanceKm;

    private Integer estimatedTravelTimeMinutes;
}