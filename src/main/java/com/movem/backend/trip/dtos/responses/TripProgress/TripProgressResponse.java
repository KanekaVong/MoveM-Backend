package com.movem.backend.trip.dtos.responses.TripProgress;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TripProgressResponse {
    String tripActivityId;
    String destination;
    String tripStatus;
    Integer progressPercentage;
    Integer totalStops;
    Integer completedStopsCount;
    TripProgressStopResponse currentStop;
    List<TripProgressStopResponse> completedStops;
    List<TripProgressStopResponse> upcomingStops;
}