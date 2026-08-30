package com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ExternalRouteResponse {

    private String tripActivityId;

    private String travelMode;

    private Boolean available;

    private String message;

    private BigDecimal distanceKm;

    private Integer estimatedTravelTimeMinutes;

    private String encodedPolyline;
}