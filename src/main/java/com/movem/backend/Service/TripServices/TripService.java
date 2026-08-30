package com.movem.backend.Service.TripServices;


import com.movem.backend.Dto.request.TripRequest.Create.CreateTripRequest;
import com.movem.backend.Dto.request.TripRequest.Update.UpdateTripRequest;
import com.movem.backend.Dto.response.TripResponses.*;
import com.movem.backend.Dto.response.TripResponses.TripProgress.TripProgressResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces.ExternalRouteResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces.NearbyPlaceResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.TripDistanceResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.TripOptimizedRouteResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.TripRouteResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.TripTravelTimeResponse;
import com.movem.backend.model.enums.Activity.ActivityStatus;

import java.util.List;

public interface TripService {

    TripResponse createTrip(CreateTripRequest request);

    TripResponse getTrip(String activityId);

    List<TripSummaryResponse> searchTrips(
            String search,
            ActivityStatus status,
            String sortBy,
            String direction,
            Boolean upcoming,
            Boolean active
    );

    TripResponse updateTrip(String activityId, UpdateTripRequest request);

    void deleteTrip(String activityId);

    TripResponse restoreTrip(String activityId);

    TripResponse completeTrip(String activityId);

    TripDistanceResponse calculateTripDistance(
            String activityId
    );

    TripTravelTimeResponse calculateTripTravelTime(
            String activityId,
            String travelMode
    );

    TripRouteResponse getTripRoute(String activityId, String travelMode);


    TripOptimizedRouteResponse optimizeTripRoute(
            String activityId,
            String travelMode
    );

    List<NearbyPlaceResponse> getNearbyPlaces(
            String activityId,
            Integer stopId,
            String type,
            Double radiusKm
    );

    ExternalRouteResponse getExternalRoute(
            String activityId,
            String travelMode
    );

    TripProgressResponse getTripProgress(
            String activityId
    );

}
