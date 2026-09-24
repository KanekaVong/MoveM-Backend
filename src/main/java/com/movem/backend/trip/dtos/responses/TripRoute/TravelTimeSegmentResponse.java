package com.movem.backend.trip.dtos.responses.TripRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelTimeSegmentResponse {

    private String from;

    private String to;

    private Double distanceKm;

    private Integer estimatedMinutes;

    private String estimatedTime;
}