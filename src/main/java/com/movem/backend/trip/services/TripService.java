package com.movem.backend.trip.services;

import com.movem.backend.trip.dtos.requests.Create.CreateTripRequest;
import com.movem.backend.trip.dtos.requests.Update.UpdateTripRequest;
import com.movem.backend.trip.dtos.responses.TripProgress.TripProgressResponse;
import com.movem.backend.trip.dtos.responses.TripResponse;
import com.movem.backend.trip.dtos.responses.TripRoute.NearByPlaces.ExternalRouteResponse;
import com.movem.backend.trip.dtos.responses.TripRoute.NearByPlaces.NearbyPlaceResponse;
import com.movem.backend.trip.dtos.responses.TripRoute.TripDistanceResponse;
import com.movem.backend.trip.dtos.responses.TripRoute.TripOptimizedRouteResponse;
import com.movem.backend.trip.dtos.responses.TripRoute.TripRouteResponse;
import com.movem.backend.trip.dtos.responses.TripRoute.TripTravelTimeResponse;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.trip.dtos.responses.TripSummaryResponse;

import java.util.List;

public interface TripService {

    TripResponse createTrip(CreateTripRequest request);

    TripResponse getTrip(String activityId);

    List<TripSummaryResponse> searchTrips(String search, ActivityStatus status, String sortBy, String direction, Boolean upcoming, Boolean active);

    TripResponse updateTrip(String activityId, UpdateTripRequest request);

    void deleteTrip(String activityId);

    TripResponse restoreTrip(String activityId);

    TripResponse completeTrip(String activityId);

    TripDistanceResponse calculateTripDistance(String activityId);

    TripTravelTimeResponse calculateTripTravelTime(String activityId, String travelMode);

    TripRouteResponse getTripRoute(String activityId, String travelMode);

    TripOptimizedRouteResponse optimizeTripRoute(String activityId, String travelMode);

    List<NearbyPlaceResponse> getNearbyPlaces(String activityId, Integer stopId, String type, Double radiusKm);

    ExternalRouteResponse getExternalRoute(String activityId, String travelMode);

    TripProgressResponse getTripProgress(String activityId);

}
