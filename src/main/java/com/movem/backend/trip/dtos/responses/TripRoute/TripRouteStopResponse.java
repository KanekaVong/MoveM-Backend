package com.movem.backend.trip.dtos.responses.TripRoute;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TripRouteStopResponse {

    private Integer sequenceOrder;
    private String locationName;

    private BigDecimal lat;
    private BigDecimal lng;

    private BigDecimal distanceFromPreviousKm;
    private Integer estimatedTravelTimeMinutes;
}