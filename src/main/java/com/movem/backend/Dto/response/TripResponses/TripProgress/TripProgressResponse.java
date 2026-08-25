package com.movem.backend.Dto.response.TripResponses.TripProgress;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TripProgressResponse {

    private String tripActivityId;

    private String destination;

    private String tripStatus;

    private Integer progressPercentage;

    private Integer totalStops;

    private Integer completedStopsCount;

    private TripProgressStopResponse currentStop;

    private List<TripProgressStopResponse> completedStops;

    private List<TripProgressStopResponse> upcomingStops;
}