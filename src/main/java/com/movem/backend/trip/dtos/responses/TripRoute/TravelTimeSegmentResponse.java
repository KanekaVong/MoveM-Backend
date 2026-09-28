package com.movem.backend.trip.dtos.responses.TripRoute;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TravelTimeSegmentResponse {
     String from;
     String to;
     Double distanceKm;
     Integer estimatedMinutes;
     String estimatedTime;
}