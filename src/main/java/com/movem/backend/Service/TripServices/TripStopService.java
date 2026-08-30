package com.movem.backend.Service.TripServices;


import com.movem.backend.Dto.request.TripRequest.Create.CreateTripStopRequest;
import com.movem.backend.Dto.request.TripRequest.Update.ReorderTripStopsRequest;
import com.movem.backend.Dto.request.TripRequest.Update.UpdateTripStopRequest;
import com.movem.backend.Dto.response.TripResponses.TripRoute.TripDirectionsResponse;
import com.movem.backend.Dto.response.TripResponses.TripStopResponse;

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
