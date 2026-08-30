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
public class TripDistanceResponse {

    private String tripActivityId;

    private Double totalDistanceKm;

    private List<DistanceSegmentResponse> segments;
}