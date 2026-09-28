package com.movem.backend.trip.dtos.responses.TripRoute;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripOptimizedRouteResponse {
    String tripActivityId;
    String destination;
    BigDecimal originalDistanceKm;
    BigDecimal optimizedDistanceKm;
    Integer originalTravelTimeMinutes;
    Integer optimizedTravelTimeMinutes;
    List<TripOptimizedStopResponse> optimizedStops;
}