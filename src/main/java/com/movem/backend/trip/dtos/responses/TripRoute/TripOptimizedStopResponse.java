package com.movem.backend.trip.dtos.responses.TripRoute;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripOptimizedStopResponse {
     Integer sequenceOrder;
     String locationName;
     BigDecimal lat;
     BigDecimal lng;
     BigDecimal distanceFromPreviousKm;
     Integer estimatedTravelTimeMinutes;
}