package com.movem.backend.Service.Implement.TripServices;

import com.movem.backend.Dto.request.TripRequest.Create.CreateTripRequest;
import com.movem.backend.Dto.request.TripRequest.Update.UpdateTripRequest;
import com.movem.backend.Dto.response.TripResponses.*;
import com.movem.backend.Dto.response.TripResponses.TripProgress.TripProgressResponse;
import com.movem.backend.Dto.response.TripResponses.TripProgress.TripProgressStopResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces.ExternalRouteResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces.GoogleRouteResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.NearByPlaces.NearbyPlaceResponse;
import com.movem.backend.Dto.response.TripResponses.TripRoute.*;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Entity.Collaboration.ActivityGroup;
import com.movem.backend.Entity.Trip.Trip;
import com.movem.backend.Entity.Trip.TripBudget;
import com.movem.backend.Entity.Trip.TripStop;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.TripMapper.TripMapper;
import com.movem.backend.Repository.CollaborationRepository.GroupRepository;
import com.movem.backend.Repository.SharedRepository.GroupMemberRepository;
import com.movem.backend.Repository.TripRepositories.TripBudgetRepository;
import com.movem.backend.Repository.TripRepositories.TripRepository;
import com.movem.backend.Repository.TripRepositories.TripStopRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.Event.Factory.Trip.TripEventFactory;
import com.movem.backend.Service.Event.FeatureEventTrackingService;
import com.movem.backend.Service.SharedServices.ActivityPermissionService;
import com.movem.backend.Service.SharedServices.ActivityService;
import com.movem.backend.Service.TripServices.NearByPlaces.NearbyPlacesService;
import com.movem.backend.Service.TripServices.TripRouteServices.TripDistanceService;
import com.movem.backend.Service.TripServices.TripRouteServices.TripRoutingService;
import com.movem.backend.Service.TripServices.TripService;
import com.movem.backend.Service.TripServices.TripRouteServices.TripTravelTimeService;
import com.movem.backend.Specification.TripSpecification;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripBudgetRepository tripBudgetRepository;
    private final GroupRepository groupRepository;
    private final TripTravelTimeService tripTravelTimeService;
    private final GroupMemberRepository groupMemberRepository;
    private final TripDistanceService tripDistanceService;
    private final TripRoutingService tripRoutingService;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final TripStopRepository tripStopRepository;
    private final TripEventFactory tripEventFactory;
    private final ActivityService activityService;
    private final ActivityPermissionService activityPermissionService;
    private final NearbyPlacesService nearbyPlacesService;
    private final CurrentUserService currentUserService;
    private final TripMapper tripMapper;

    @Override
    public TripResponse createTrip(
            CreateTripRequest request
    ) {

        User user =
                currentUserService.getCurrentUser();

        Activity activity =
                activityService.createActivity(
                        request,
                        user,
                        ActivityType.TRIP
                );

        Trip trip = new Trip();

        trip.setActivity(activity);

        trip.setDestination(
                request.getDestination() != null
                        ? request.getDestination()
                        : request.getLocationName()
        );

        trip.setFlightNumber(
                request.getFlightNumber()
        );

        trip.setHotelName(
                request.getHotelName()
        );

        Trip saved =
                tripRepository.save(trip);

        featureEventTrackingService.handle(
                tripEventFactory.created(
                        saved,
                        user
                )
        );

        return enrich(
                tripMapper.toResponse(saved),
                saved
        );
    }

    @Override
    public TripResponse getTrip(String activityId) {

        User user = currentUserService.getCurrentUser();
        Trip trip = findActiveTripOrThrow(activityId);

        activityPermissionService.validateCanEditActivity(trip.getActivity(), user);

        return enrich(tripMapper.toResponse(trip), trip);
    }

    @Override
    public List<TripSummaryResponse> searchTrips(
            String search,
            ActivityStatus status,
            String sortBy,
            String direction,
            Boolean upcoming,
            Boolean active
    ) {

        User user =
                currentUserService.getCurrentUser();

        Specification<Trip> spec =
                Specification
                        .where(
                                TripSpecification.forUser(user)
                        )
                        .and(
                                TripSpecification.notDeleted()
                        )
                        .and(
                                TripSpecification.hasStatus(status)
                        )
                        .and(
                                TripSpecification.matchesSearch(search)
                        );

        if (Boolean.TRUE.equals(upcoming)) {

            spec = spec.and(
                    TripSpecification.isUpcoming()
            );
        }

        if (Boolean.TRUE.equals(active)) {

            spec = spec.and(
                    TripSpecification.isActive()
            );
        }

        List<Trip> trips =
                tripRepository.findAll(
                        spec,
                        buildSort(
                                sortBy,
                                direction
                        )
                );

        return trips.stream()
                .map(tripMapper::toSummaryResponse)
                .toList();
    }

    @Override
    public TripRouteResponse getTripRoute(
            String activityId,
            String travelMode
    ) {

        Trip trip = findTripOrThrow(activityId);

        List<TripStop> stops =
                new ArrayList<>(trip.getStops());

        stops.sort(
                Comparator.comparing(
                        TripStop::getSequenceOrder
                )
        );

        if (stops.size() < 2) {

            throw new IllegalArgumentException(
                    "At least two stops are required."
            );
        }

        GoogleRouteResponse googleResponse =
                tripRoutingService.calculateMultiStopRoute(
                        stops,
                        travelMode
                );

        GoogleRouteResponse.GoogleRoute googleRoute = null;

        if (googleResponse != null
                && googleResponse.getRoutes() != null
                && !googleResponse.getRoutes().isEmpty()) {

            googleRoute =
                    googleResponse
                            .getRoutes()
                            .get(0);
        }

        String encodedPolyline = null;

        if (googleRoute != null
                && googleRoute.getPolyline() != null) {

            encodedPolyline =
                    googleRoute
                            .getPolyline()
                            .getEncodedPolyline();
        }

        List<GoogleRouteResponse.GoogleLeg> legs =
                googleRoute != null
                        && googleRoute.getLegs() != null
                        ? googleRoute.getLegs()
                        : java.util.Collections.emptyList();


        List<TripRouteStopResponse> routeStops =
                new ArrayList<>();

        List<RouteSegmentResponse> segments =
                new ArrayList<>();

        double totalDistance = 0.0;
        double totalMinutes = 0.0;


        for (int i = 0; i < stops.size(); i++) {

            TripStop currentStop =
                    stops.get(i);

            double distanceFromPrevious = 0.0;
            int estimatedMinutes = 0;

            if (i > 0) {

                TripStop previousStop =
                        stops.get(i - 1);

                int legIndex =
                        i - 1;

                if (legIndex < legs.size()) {

                    GoogleRouteResponse.GoogleLeg leg =
                            legs.get(legIndex);

                    if (leg.getDistanceMeters() != null) {

                        distanceFromPrevious =
                                leg.getDistanceMeters()
                                        / 1000.0;
                    }

                    if (leg.getDuration() != null) {

                        estimatedMinutes =
                                (int) Math.ceil(
                                        parseDurationToSeconds(
                                                leg.getDuration()
                                        ) / 60.0
                                );
                    }

                } else {

                    distanceFromPrevious =
                            tripDistanceService
                                    .calculateDistanceKm(
                                            previousStop,
                                            currentStop
                                    );

                    double fallbackMinutes =
                            tripTravelTimeService
                                    .calculateTravelTimeMinutes(
                                            distanceFromPrevious,
                                            travelMode
                                    );

                    estimatedMinutes =
                            (int) Math.ceil(
                                    fallbackMinutes
                            );
                }


                totalDistance +=
                        distanceFromPrevious;

                totalMinutes +=
                        estimatedMinutes;


                segments.add(
                        RouteSegmentResponse.builder()

                                .sequenceOrder(
                                        currentStop
                                                .getSequenceOrder()
                                )

                                .from(
                                        previousStop
                                                .getLocationName()
                                )

                                .to(
                                        currentStop
                                                .getLocationName()
                                )

                                .distanceKm(
                                        BigDecimal.valueOf(
                                                Math.round(
                                                        distanceFromPrevious
                                                                * 100.0
                                                ) / 100.0
                                        )
                                )

                                .estimatedTravelTimeMinutes(
                                        estimatedMinutes
                                )

                                .build()
                );
            }

            routeStops.add(
                    TripRouteStopResponse.builder()

                            .sequenceOrder(
                                    currentStop
                                            .getSequenceOrder()
                            )

                            .locationName(
                                    currentStop
                                            .getLocationName()
                            )

                            .lat(
                                    currentStop.getLat()
                            )

                            .lng(
                                    currentStop.getLng()
                            )

                            .distanceFromPreviousKm(
                                    BigDecimal.valueOf(
                                            Math.round(
                                                    distanceFromPrevious
                                                            * 100.0
                                            ) / 100.0
                                    )
                            )

                            .estimatedTravelTimeMinutes(
                                    estimatedMinutes
                            )

                            .build()
            );
        }

        return TripRouteResponse.builder()

                .tripActivityId(
                        trip.getActivityId()
                )

                .destination(
                        trip.getDestination()
                )

                .totalDistanceKm(
                        BigDecimal.valueOf(
                                Math.round(
                                        totalDistance
                                                * 100.0
                                ) / 100.0
                        )
                )

                .estimatedTravelTimeMinutes(
                        (int) Math.ceil(
                                totalMinutes
                        )
                )

                .encodedPolyline(
                        encodedPolyline
                )

                .stops(
                        routeStops
                )

                .segments(
                        segments
                )

                .build();
    }

    @Transactional
    @Override
    public TripProgressResponse getTripProgress(
            String activityId
    ) {

        Trip trip = findTripOrThrow(activityId);

        List<TripStop> stops =
                tripStopRepository
                        .findByTripOrderBySequenceOrderAsc(trip);

        int totalStops = stops.size();

        if (totalStops == 0) {

            return TripProgressResponse.builder()
                    .tripActivityId(trip.getActivityId())
                    .destination(trip.getDestination())
                    .tripStatus("NOT_STARTED")
                    .progressPercentage(0)
                    .totalStops(0)
                    .completedStopsCount(0)
                    .currentStop(null)
                    .completedStops(new ArrayList<>())
                    .upcomingStops(new ArrayList<>())
                    .build();
        }

        List<TripStop> completedStops =
                stops.stream()
                        .filter(stop ->
                                Boolean.TRUE.equals(
                                        stop.getIsCompleted()
                                )
                        )
                        .toList();

        List<TripStop> upcomingStops =
                stops.stream()
                        .filter(stop ->
                                !Boolean.TRUE.equals(
                                        stop.getIsCompleted()
                                )
                        )
                        .toList();

        TripStop currentStop =
                upcomingStops.isEmpty()
                        ? null
                        : upcomingStops.get(0);

        int completedStopsCount =
                completedStops.size();

        int progressPercentage =
                (int) Math.round(
                        (completedStopsCount * 100.0)
                                / totalStops
                );

        String tripStatus;

        if (completedStopsCount == 0) {

            tripStatus = "NOT_STARTED";

        } else if (completedStopsCount == totalStops) {

            tripStatus = "COMPLETED";

        } else {

            tripStatus = "IN_PROGRESS";
        }

        return TripProgressResponse.builder()

                .tripActivityId(
                        trip.getActivityId()
                )

                .destination(
                        trip.getDestination()
                )

                .tripStatus(
                        tripStatus
                )

                .progressPercentage(
                        progressPercentage
                )

                .totalStops(
                        totalStops
                )

                .completedStopsCount(
                        completedStopsCount
                )

                .currentStop(
                        currentStop == null
                                ? null
                                : toProgressStopResponse(
                                currentStop
                        )
                )

                .completedStops(
                        completedStops.stream()
                                .map(
                                        this::toProgressStopResponse
                                )
                                .toList()
                )

                .upcomingStops(
                        upcomingStops.stream()
                                .map(
                                        this::toProgressStopResponse
                                )
                                .toList()
                )

                .build();
    }

    private TripProgressStopResponse toProgressStopResponse(
            TripStop stop
    ) {

        return TripProgressStopResponse.builder()

                .id(
                        stop.getId()
                )

                .sequenceOrder(
                        stop.getSequenceOrder()
                )

                .locationName(
                        stop.getLocationName()
                )

                .locationAddress(
                        stop.getLocationAddress()
                )

                .lat(
                        stop.getLat()
                )

                .lng(
                        stop.getLng()
                )

                .isCompleted(
                        stop.getIsCompleted()
                )

                .build();
    }



    @Override
    public TripResponse updateTrip(
            String activityId,
            UpdateTripRequest request
    ) {

        User user =
                currentUserService.getCurrentUser();

        Trip trip =
                findTripOrThrow(activityId);

        activityPermissionService.validateCanEditActivity(
                trip.getActivity(),
                user
        );

        Activity activity =
                activityService.updateActivity(
                        trip.getActivity(),
                        request
                );

        trip.setDestination(
                request.getDestination()
        );

        trip.setFlightNumber(
                request.getFlightNumber()
        );

        trip.setHotelName(
                request.getHotelName()
        );

        Trip saved =
                tripRepository.save(trip);

        featureEventTrackingService.handle(
                tripEventFactory.updated(
                        saved,
                        user
                )
        );

        return enrich(
                tripMapper.toResponse(saved),
                saved
        );
    }

    @Override
    public void deleteTrip(
            String activityId
    ) {

        User user =
                currentUserService.getCurrentUser();

        Trip trip =
                findTripOrThrow(activityId);

        activityPermissionService.validateActivityOwner(
                trip.getActivity(),
                user
        );

        Activity activity =
                trip.getActivity();

        if (activity.getStatus() == ActivityStatus.DELETED) {

            throw new IllegalStateException(
                    "Trip is already deleted."
            );
        }

        activity.setStatus(
                ActivityStatus.DELETED
        );

        activity.setDeletedAt(
                LocalDateTime.now()
        );

        featureEventTrackingService.handle(
                tripEventFactory.deleted(
                        trip,
                        user
                )
        );
    }

    @Override
    public TripResponse restoreTrip(
            String activityId
    ) {

        User user =
                currentUserService.getCurrentUser();

        Trip trip =
                findTripOrThrow(activityId);

        activityPermissionService.validateActivityOwner(
                trip.getActivity(),
                user
        );

        Activity activity =
                trip.getActivity();

        if (activity.getStatus() != ActivityStatus.DELETED) {

            throw new IllegalStateException(
                    "Trip is already restored."
            );
        }

        activity.setStatus(
                ActivityStatus.PENDING
        );

        activity.setDeletedAt(null);

        featureEventTrackingService.handle(
                tripEventFactory.restored(
                        trip,
                        user
                )
        );

        return enrich(
                tripMapper.toResponse(trip),
                trip
        );
    }

    @Override
    public TripResponse completeTrip(
            String activityId
    ) {

        User user =
                currentUserService.getCurrentUser();

        Trip trip =
                findTripOrThrow(activityId);

        activityPermissionService.validateCanEditActivity(
                trip.getActivity(),
                user
        );

        Activity activity =
                trip.getActivity();

        if (activity.getStatus()
                == ActivityStatus.COMPLETE) {

            throw new IllegalStateException(
                    "Trip is already marked complete."
            );
        }

        activity.setStatus(
                ActivityStatus.COMPLETE
        );

        return enrich(
                tripMapper.toResponse(trip),
                trip
        );
    }

    @Override
    public TripDistanceResponse calculateTripDistance(
            String activityId
    ) {

        Trip trip = findTripOrThrow(activityId);

        List<TripStop> stops = trip.getStops();

        double totalDistance =
                tripDistanceService.calculateTotalDistanceKm(
                        stops
                );

        List<DistanceSegmentResponse> segments =
                new ArrayList<>();

        if (stops.size() >= 2) {

            for (int i = 0; i < stops.size() - 1; i++) {

                TripStop from = stops.get(i);
                TripStop to = stops.get(i + 1);

                double distance =
                        tripDistanceService.calculateDistanceKm(
                                from,
                                to
                        );

                segments.add(
                        DistanceSegmentResponse.builder()
                                .from(from.getLocationName())
                                .to(to.getLocationName())
                                .distanceKm(
                                        Math.round(distance * 100.0) / 100.0
                                )
                                .build()
                );
            }
        }

        return TripDistanceResponse.builder()
                .tripActivityId(
                        trip.getActivityId()
                )
                .totalDistanceKm(
                        Math.round(totalDistance * 100.0) / 100.0
                )
                .segments(segments)
                .build();
    }

    @Override
    public TripTravelTimeResponse calculateTripTravelTime(
            String activityId,
            String travelMode
    ) {

        Trip trip = findTripOrThrow(activityId);

        List<TripStop> stops = trip.getStops();

        double totalDistance =
                tripDistanceService.calculateTotalDistanceKm(
                        stops
                );

        double totalMinutes = 0;

        List<TravelTimeSegmentResponse> segments =
                new ArrayList<>();

        if (stops.size() >= 2) {

            for (int i = 0; i < stops.size() - 1; i++) {

                TripStop from = stops.get(i);
                TripStop to = stops.get(i + 1);

                double distance =
                        tripDistanceService.calculateDistanceKm(
                                from,
                                to
                        );

                double minutes =
                        tripTravelTimeService
                                .calculateTravelTimeMinutes(
                                        distance,
                                        travelMode
                                );

                int roundedMinutes =
                        (int) Math.ceil(minutes);

                totalMinutes += minutes;

                segments.add(
                        TravelTimeSegmentResponse.builder()
                                .from(from.getLocationName())
                                .to(to.getLocationName())
                                .distanceKm(
                                        Math.round(distance * 100.0) / 100.0
                                )
                                .estimatedMinutes(roundedMinutes)
                                .estimatedTime(
                                        formatDuration(roundedMinutes)
                                )
                                .build()
                );
            }
        }

        int roundedTotalMinutes =
                (int) Math.ceil(totalMinutes);

        return TripTravelTimeResponse.builder()
                .tripActivityId(trip.getActivityId())
                .travelMode(travelMode.toUpperCase())
                .totalDistanceKm(
                        Math.round(totalDistance * 100.0) / 100.0
                )
                .estimatedTotalMinutes(
                        roundedTotalMinutes
                )
                .estimatedTotalTime(
                        formatDuration(roundedTotalMinutes)
                )
                .segments(segments)
                .build();
    }

    @Override
    public TripOptimizedRouteResponse optimizeTripRoute(
            String activityId,
            String travelMode
    ) {

        Trip trip = findTripOrThrow(activityId);

        List<TripStop> stops = new ArrayList<>(
                trip.getStops()
        );

        // Original planned order
        stops.sort(
                Comparator.comparing(
                        TripStop::getSequenceOrder
                )
        );

        if (stops.size() < 2) {

            List<TripOptimizedStopResponse> responseStops =
                    stops.stream()
                            .map(stop ->
                                    TripOptimizedStopResponse.builder()
                                            .sequenceOrder(
                                                    stop.getSequenceOrder()
                                            )
                                            .locationName(
                                                    stop.getLocationName()
                                            )
                                            .lat(stop.getLat())
                                            .lng(stop.getLng())
                                            .distanceFromPreviousKm(
                                                    BigDecimal.ZERO
                                            )
                                            .estimatedTravelTimeMinutes(0)
                                            .build()
                            )
                            .toList();

            return TripOptimizedRouteResponse.builder()
                    .tripActivityId(
                            trip.getActivityId()
                    )
                    .destination(
                            trip.getDestination()
                    )
                    .originalDistanceKm(
                            BigDecimal.ZERO
                    )
                    .optimizedDistanceKm(
                            BigDecimal.ZERO
                    )
                    .originalTravelTimeMinutes(0)
                    .optimizedTravelTimeMinutes(0)
                    .optimizedStops(responseStops)
                    .build();
        }

        double originalDistance =
                tripDistanceService.calculateTotalDistanceKm(
                        stops
                );

        double originalTravelTime = 0;

        for (int i = 0; i < stops.size() - 1; i++) {

            TripStop from = stops.get(i);
            TripStop to = stops.get(i + 1);

            double distance =
                    tripDistanceService.calculateDistanceKm(
                            from,
                            to
                    );

            originalTravelTime +=
                    tripTravelTimeService
                            .calculateTravelTimeMinutes(
                                    distance,
                                    travelMode
                            );
        }

        List<TripStop> optimizedStops =
                new ArrayList<>();

        TripStop current = stops.get(0);

        // Keep the first stop fixed
        optimizedStops.add(current);

        List<TripStop> remaining =
                new ArrayList<>(
                        stops.subList(1, stops.size())
                );

        while (!remaining.isEmpty()) {

            TripStop nearest = null;
            double shortestDistance =
                    Double.MAX_VALUE;

            for (TripStop candidate : remaining) {

                double distance =
                        tripDistanceService.calculateDistanceKm(
                                current,
                                candidate
                        );

                if (distance < shortestDistance) {

                    shortestDistance = distance;
                    nearest = candidate;
                }
            }

            if (nearest == null) {
                break;
            }

            optimizedStops.add(nearest);
            remaining.remove(nearest);

            current = nearest;
        }

        // ---------------------------------------------
        // OPTIMIZED ROUTE CALCULATION
        // ---------------------------------------------

        double optimizedDistance = 0;
        double optimizedTravelTime = 0;

        List<TripOptimizedStopResponse> responseStops =
                new ArrayList<>();

        for (int i = 0; i < optimizedStops.size(); i++) {

            TripStop currentStop =
                    optimizedStops.get(i);

            double distanceFromPrevious = 0;
            int estimatedTravelTimeMinutes = 0;

            if (i > 0) {

                TripStop previousStop =
                        optimizedStops.get(i - 1);

                distanceFromPrevious =
                        tripDistanceService.calculateDistanceKm(
                                previousStop,
                                currentStop
                        );

                double travelTime =
                        tripTravelTimeService
                                .calculateTravelTimeMinutes(
                                        distanceFromPrevious,
                                        travelMode
                                );

                estimatedTravelTimeMinutes =
                        (int) Math.ceil(travelTime);

                optimizedDistance +=
                        distanceFromPrevious;

                optimizedTravelTime +=
                        travelTime;
            }

            responseStops.add(
                    TripOptimizedStopResponse.builder()
                            .sequenceOrder(i + 1)
                            .locationName(
                                    currentStop.getLocationName()
                            )
                            .lat(currentStop.getLat())
                            .lng(currentStop.getLng())
                            .distanceFromPreviousKm(
                                    BigDecimal.valueOf(
                                            Math.round(
                                                    distanceFromPrevious * 100
                                            ) / 100.0
                                    )
                            )
                            .estimatedTravelTimeMinutes(
                                    estimatedTravelTimeMinutes
                            )
                            .build()
            );
        }

        // ---------------------------------------------
        // RESPONSE
        // ---------------------------------------------

        return TripOptimizedRouteResponse.builder()
                .tripActivityId(
                        trip.getActivityId()
                )
                .destination(
                        trip.getDestination()
                )
                .originalDistanceKm(
                        BigDecimal.valueOf(
                                Math.round(
                                        originalDistance * 100
                                ) / 100.0
                        )
                )
                .optimizedDistanceKm(
                        BigDecimal.valueOf(
                                Math.round(
                                        optimizedDistance * 100
                                ) / 100.0
                        )
                )
                .originalTravelTimeMinutes(
                        (int) Math.ceil(
                                originalTravelTime
                        )
                )
                .optimizedTravelTimeMinutes(
                        (int) Math.ceil(
                                optimizedTravelTime
                        )
                )
                .optimizedStops(
                        responseStops
                )
                .build();
    }

    @Override
    public ExternalRouteResponse getExternalRoute(
            String activityId,
            String travelMode
    ) {

        Trip trip = findTripOrThrow(activityId);

        List<TripStop> stops =
                new ArrayList<>(trip.getStops());

        stops.sort(
                Comparator.comparing(
                        TripStop::getSequenceOrder
                )
        );

        if (stops.size() < 2) {
            throw new IllegalArgumentException(
                    "At least two stops are required."
            );
        }

        TripStop origin = stops.get(0);
        TripStop destination = stops.get(1);

        String mode = travelMode.trim().toUpperCase();

        GoogleRouteResponse googleResponse =
                tripRoutingService.calculateRoute(
                        origin,
                        destination,
                        mode
                );

        if (googleResponse == null
                || googleResponse.getRoutes() == null
                || googleResponse.getRoutes().isEmpty()) {

            return ExternalRouteResponse.builder()
                    .tripActivityId(
                            trip.getActivityId()
                    )
                    .travelMode(mode)
                    .available(false)
                    .message(
                            "No "
                                    + mode.toLowerCase()
                                    + " route is available "
                                    + "for this location."
                    )
                    .distanceKm(null)
                    .estimatedTravelTimeMinutes(null)
                    .encodedPolyline(null)
                    .build();
        }

        GoogleRouteResponse.GoogleRoute route =
                googleResponse.getRoutes().get(0);

        double distanceKm =
                route.getDistanceMeters() / 1000.0;

        int travelTimeMinutes =
                (int) Math.ceil(
                        parseDurationToSeconds(
                                route.getDuration()
                        ) / 60.0
                );

        return ExternalRouteResponse.builder()
                .tripActivityId(
                        trip.getActivityId()
                )
                .travelMode(mode)
                .available(true)
                .message("Route found successfully.")
                .distanceKm(
                        BigDecimal.valueOf(
                                Math.round(
                                        distanceKm * 100.0
                                ) / 100.0
                        )
                )
                .estimatedTravelTimeMinutes(
                        travelTimeMinutes
                )
                .encodedPolyline(
                        route.getPolyline()
                                .getEncodedPolyline()
                )
                .build();
    }

    @Override
    public List<NearbyPlaceResponse> getNearbyPlaces(
            String activityId,
            Integer stopId,
            String type,
            Double radiusKm
    ) {

        Trip trip = findTripOrThrow(activityId);

        TripStop stop = trip.getStops()
                .stream()
                .filter(s -> s.getId().equals(stopId))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Trip stop not found"
                        )
                );

        return nearbyPlacesService.findNearbyPlaces(
                stop.getLat(),
                stop.getLng(),
                type,
                radiusKm != null ? radiusKm : 5.0
        );
    }

    private Trip findTripOrThrow(String activityId) {
        return tripRepository.findByActivityId(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + activityId));
    }

    private Trip findActiveTripOrThrow(String activityId) {

        Trip trip = findTripOrThrow(activityId);

        if (trip.getActivity().getStatus() == ActivityStatus.DELETED) {
            throw new ResourceNotFoundException(
                    "Trip not found: " + activityId
            );
        }

        return trip;
    }

    private Sort buildSort(String sortBy, String direction) {
        String property = switch (sortBy == null ? "" : sortBy) {
            case "name" -> "activity.activityName";
            case "deadline" -> "activity.deadline";
            default -> "activity.startActivity";
        };
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, property);
    }

    private String formatDuration(int minutes) {

        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        if (hours == 0) {
            return remainingMinutes + " min";
        }

        if (remainingMinutes == 0) {
            return hours + " hr";
        }

        return hours + " hr "
                + remainingMinutes
                + " min";
    }

    private String mapToGoogleTravelMode(String travelMode) {

        if (travelMode == null || travelMode.isBlank()) {
            throw new IllegalArgumentException(
                    "Travel mode is required."
            );
        }

        return switch (travelMode.toUpperCase()) {

            case "WALKING" -> "WALK";

            case "CYCLING" -> "BICYCLE";

            case "DRIVING" -> "DRIVE";

            case "RIDING" -> "TWO_WHEELER";

            default -> throw new IllegalArgumentException(
                    "Unsupported travel mode: " + travelMode
            );
        };
    }

    private long parseDurationToSeconds(
            String duration
    ) {

        if (duration == null || duration.isBlank()) {
            return 0;
        }

        String value =
                duration.replace("s", "");

        return Math.round(
                Double.parseDouble(value)
        );
    }

    private TripResponse enrich(TripResponse response, Trip trip) {

        Optional<ActivityGroup> group = groupRepository.findByActivity(trip.getActivity());
        int memberCount = group
                .map(groupMemberRepository::countByActivityGroup)
                .map(Long::intValue)
                .orElse(1);

        response.setMemberCount(memberCount);

        List<TripBudget> budgets = tripBudgetRepository.findByTrip(trip);

        BigDecimal totalAllocated = budgets.stream()
                .map(TripBudget::getAllocatedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalSpent = budgets.stream()
                .map(b -> b.getSpentAmount() == null ? BigDecimal.ZERO : b.getSpentAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setTotalAllocatedBudget(totalAllocated);
        response.setTotalSpent(totalSpent);
        response.setPerPersonShare(
                memberCount > 0
                        ? totalAllocated.divide(BigDecimal.valueOf(memberCount), 2, RoundingMode.HALF_UP)
                        : totalAllocated
        );

        return response;
    }
}
