package com.movem.backend.Service.Implement.TripServices;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripStopRequest;
import com.movem.backend.Dto.request.TripRequest.Update.ReorderTripStopsRequest;
import com.movem.backend.Dto.request.TripRequest.Update.UpdateTripStopRequest;
import com.movem.backend.Dto.response.TripResponses.TripRoute.TripDirectionsResponse;
import com.movem.backend.Dto.response.TripResponses.TripStopResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripStop;
import com.movem.backend.Exception.BadRequestException;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.TripMapper.TripStopMapper;
import com.movem.backend.Repository.TripRepositories.TripRepository;
import com.movem.backend.Repository.TripRepositories.TripStopRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.Event.Factory.Trip.TripEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.Service.SharedServices.ActivityPermissionService;
import com.movem.backend.Service.TripServices.TripStopService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TripStopServiceImpl implements TripStopService {

    private final TripRepository tripRepository;
    private final TripStopRepository tripStopRepository;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final TripEventFactory tripEventFactory;
    private final ActivityPermissionService activityPermissionService;
    private final CurrentUserService currentUserService;
    private final TripStopMapper tripStopMapper;

    @Override
    public TripStopResponse addStop(String tripActivityId, CreateTripStopRequest request) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        int nextSequence = request.getSequenceOrder() != null
                ? request.getSequenceOrder()
                : tripStopRepository.countByTrip(trip) + 1;

        TripStop stop = new TripStop();
        stop.setTrip(trip);
        stop.setLocationName(request.getLocationName());
        stop.setSequenceOrder(nextSequence);
        stop.setArrivalTime(request.getArrivalTime());
        stop.setDepartureTime(request.getDepartureTime());
        stop.setLocationAddress(request.getLocationAddress());
        stop.setLat(request.getLat());
        stop.setLng(request.getLng());
        stop.setGooglePlaceId(request.getGooglePlaceId());
        stop.setCoordinates(request.getCoordinates());
        stop.setIsCompleted(false);

        tripStopRepository.save(stop);

        featureEventTrackingService.handle(
                tripEventFactory.stopAdded(
                        stop,
                        user
                )
        );

        return tripStopMapper.toResponse(stop);
    }

    @Override
    public List<TripStopResponse> getStops(String tripActivityId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        return tripStopMapper.toResponseList(
                tripStopRepository.findByTripOrderBySequenceOrderAsc(trip)
        );
    }

    @Override
    public TripStopResponse updateStop(String tripActivityId, Integer stopId, UpdateTripStopRequest request) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        TripStop stop = findStopOrThrow(trip, stopId);

        stop.setLocationName(request.getLocationName());
        stop.setArrivalTime(request.getArrivalTime());
        stop.setDepartureTime(request.getDepartureTime());
        stop.setLocationAddress(request.getLocationAddress());
        stop.setLat(request.getLat());
        stop.setLng(request.getLng());
        stop.setGooglePlaceId(request.getGooglePlaceId());
        stop.setCoordinates(request.getCoordinates());
        if (request.getIsCompleted() != null) {
            stop.setIsCompleted(request.getIsCompleted());
        }

        return tripStopMapper.toResponse(stop);
    }

    @Override
    public void removeStop(String tripActivityId, Integer stopId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        TripStop stop = findStopOrThrow(trip, stopId);
        tripStopRepository.delete(stop);
    }

    @Override
    public TripStopResponse completeStop(String tripActivityId, Integer stopId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        TripStop stop =
                findStopOrThrow(
                        trip,
                        stopId
                );

        if (Boolean.TRUE.equals(stop.getIsCompleted())) {
            throw new IllegalStateException(
                    "Trip stop is already completed."
            );
        }

        stop.setIsCompleted(true);

        featureEventTrackingService.handle(
                tripEventFactory.stopCompleted(
                        stop,
                        user
                )
        );

        return tripStopMapper.toResponse(stop);
    }

    @Override
    public List<TripStopResponse> reorderStops(String tripActivityId, ReorderTripStopsRequest request) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        List<TripStop> stops = tripStopRepository.findByTripOrderBySequenceOrderAsc(trip);
        Map<Integer, TripStop> byId = stops.stream()
                .collect(Collectors.toMap(TripStop::getId, s -> s));

        if (request.getStopIds().size() != stops.size()
                || !byId.keySet().containsAll(request.getStopIds())) {
            throw new BadRequestException("stopIds must include every existing stop exactly once");
        }

        int order = 1;
        for (Integer stopId : request.getStopIds()) {
            byId.get(stopId).setSequenceOrder(order++);
        }

        return tripStopMapper.toResponseList(
                tripStopRepository.findByTripOrderBySequenceOrderAsc(trip)
        );
    }

    @Override
    public TripDirectionsResponse getDirections(String tripActivityId, Integer stopId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        TripStop stop = findStopOrThrow(trip, stopId);

        if (stop.getLat() == null || stop.getLng() == null) {
            throw new BadRequestException("This stop has no coordinates yet");
        }

        StringBuilder url = new StringBuilder("https://www.google.com/maps/dir/?api=1&destination=")
                .append(stop.getLat()).append(",").append(stop.getLng());

        if (stop.getGooglePlaceId() != null && !stop.getGooglePlaceId().isBlank()) {
            url.append("&destination_place_id=").append(stop.getGooglePlaceId());
        }

        return TripDirectionsResponse.builder()
                .mapsUrl(url.toString())
                .destinationLat(stop.getLat())
                .destinationLng(stop.getLng())
                .googlePlaceId(stop.getGooglePlaceId())
                .build();
    }

    private Trip findTripOrThrow(String tripActivityId) {
        return tripRepository.findByActivityId(tripActivityId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + tripActivityId));
    }

    private TripStop findStopOrThrow(Trip trip, Integer stopId) {
        return tripStopRepository.findByIdAndTrip(stopId, trip)
                .orElseThrow(() -> new ResourceNotFoundException("Stop not found: " + stopId));
    }
}
