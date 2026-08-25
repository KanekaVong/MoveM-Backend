package com.movem.backend.Dto.response.TripResponses.TripRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TripTravelTimeResponse {

    private String tripActivityId;

    private String travelMode;

    private Double totalDistanceKm;

    private Integer estimatedTotalMinutes;

    private String estimatedTotalTime;

    private List<TravelTimeSegmentResponse> segments;
}