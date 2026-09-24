package com.movem.backend.trip.services;


import com.movem.backend.trip.dtos.requests.Create.CreateTripStopRequest;
import com.movem.backend.trip.dtos.requests.Update.ReorderTripStopsRequest;
import com.movem.backend.trip.dtos.requests.Update.UpdateTripStopRequest;
import com.movem.backend.trip.dtos.responses.TripRoute.TripDirectionsResponse;
import com.movem.backend.trip.dtos.responses.TripStopResponse;

import java.util.List;

public interface TripStopService {

    TripStopResponse addStop(String tripActivityId, CreateTripStopRequest request);

    List<TripStopResponse> getStops(String tripActivityId);

    TripStopResponse updateStop(String tripActivityId, Integer stopId, UpdateTripStopRequest request);

    void removeStop(String tripActivityId, Integer stopId);

    TripStopResponse completeStop(String tripActivityId, Integer stopId);

    List<TripStopResponse> reorderStops(String tripActivityId, ReorderTripStopsRequest request);

    TripDirectionsResponse getDirections(String tripActivityId, Integer stopId);

}
