package com.movem.backend.trip.services.impl;

import com.movem.backend.trip.dtos.requests.Create.CreateTripStopRequest;
import com.movem.backend.trip.dtos.requests.Update.ReorderTripStopsRequest;
import com.movem.backend.trip.dtos.requests.Update.UpdateTripStopRequest;
import com.movem.backend.trip.dtos.responses.TripRoute.TripDirectionsResponse;
import com.movem.backend.trip.dtos.responses.TripStopResponse;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.trip.entities.TripStop;
import com.movem.backend.commons.Exception.BadRequestException;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.trip.mappers.TripStopMapper;
import com.movem.backend.trip.repositories.TripRepository;
import com.movem.backend.trip.repositories.TripStopRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.commons.Event.Factory.Trip.TripEventFactory;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.shared.activity.services.ActivityPermissionService;
import com.movem.backend.trip.services.TripStopService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        trip.getStops().removeIf(existingStop -> existingStop.getId().equals(stopId));

        tripStopRepository.delete(stop);
        tripStopRepository.flush();
    }

    @Override
    public TripStopResponse completeStop(String tripActivityId, Integer stopId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        TripStop stop = findStopOrThrow(trip, stopId);

        if (Boolean.TRUE.equals(stop.getIsCompleted())) {
            throw new IllegalStateException("Trip stop is already completed.");
        }

        stop.setIsCompleted(true);

        featureEventTrackingService.handle(tripEventFactory.stopCompleted(stop, user));

        return tripStopMapper.toResponse(stop);
    }

    @Override
    public List<TripStopResponse> reorderStops(String tripActivityId, ReorderTripStopsRequest request) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findTripOrThrow(tripActivityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        List<TripStop> stops = tripStopRepository.findByTripOrderBySequenceOrderAsc(trip);
        Map<Integer, TripStop> byId = stops.stream().collect(Collectors.toMap(TripStop::getId, s -> s));

        if (request.getStopIds().size() != stops.size() || !byId.keySet().containsAll(request.getStopIds())) {
            throw new BadRequestException("stopIds must include every existing stop exactly once");
        }

        int order = 1;
        for (Integer stopId : request.getStopIds()) {
            byId.get(stopId).setSequenceOrder(order++);
        }

        return tripStopMapper.toResponseList(tripStopRepository.findByTripOrderBySequenceOrderAsc(trip));
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

    private void deleteStopOrThrow(Trip trip, Integer stopId) {
        if (!tripStopRepository.findByIdAndTrip(stopId, trip).isPresent()) {
            throw new ResourceNotFoundException("Stop not found: " + stopId);
        }

        tripStopRepository.deleteByIdAndTrip(stopId, trip);
        tripStopRepository.flush();
    }
}
