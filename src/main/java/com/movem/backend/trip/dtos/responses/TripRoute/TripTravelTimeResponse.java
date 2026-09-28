package com.movem.backend.trip.dtos.responses.TripRoute;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripTravelTimeResponse {
     String tripActivityId;
     String travelMode;
     Double totalDistanceKm;
     Integer estimatedTotalMinutes;
     String estimatedTotalTime;
     List<TravelTimeSegmentResponse> segments;
}