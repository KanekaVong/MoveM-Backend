package com.movem.backend.Dto.response.TripResponses.TripRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistanceSegmentResponse {

    private String from;

    private String to;

    private Double distanceKm;
}