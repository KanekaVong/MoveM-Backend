package com.movem.backend.Controller.TripControllers;

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
import com.movem.backend.Service.TripServices.TripService;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@Tag( name = "Trip - Trip Creations, Optimized Routes",
      description = "Create trip, add collaborators, plan trips seamlessly")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@Valid @RequestBody CreateTripRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(request));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<TripResponse> getTrip(@PathVariable String activityId) {
        return ResponseEntity.ok(tripService.getTrip(activityId));
    }

    @GetMapping("/{activityId}/distance")
    public ResponseEntity<TripDistanceResponse> getTripDistance(
            @PathVariable String activityId
    ) {
        return ResponseEntity.ok(
                tripService.calculateTripDistance(activityId)
        );
    }

    @GetMapping("/{activityId}/travel-time")
    public ResponseEntity<TripTravelTimeResponse> getTripTravelTime(
            @PathVariable String activityId,
            @RequestParam(defaultValue = "DRIVING")
            String mode
    ) {

        return ResponseEntity.ok(
                tripService.calculateTripTravelTime(
                        activityId,
                        mode
                )
        );
    }

    @GetMapping("/{activityId}/route")
    public ResponseEntity<TripRouteResponse> getTripRoute(
            @PathVariable String activityId,
            @RequestParam(defaultValue = "DRIVING")
            String travelMode
    ) {

        return ResponseEntity.ok(
                tripService.getTripRoute(
                        activityId,
                        travelMode
                )
        );
    }

    @GetMapping("/{activityId}/progress")
    public ResponseEntity<TripProgressResponse> getTripProgress(
            @PathVariable String activityId
    ) {

        return ResponseEntity.ok(
                tripService.getTripProgress(activityId)
        );
    }

    @GetMapping("/{activityId}/route/optimize")
    public ResponseEntity<TripOptimizedRouteResponse> optimizeTripRoute(
            @PathVariable String activityId,
            @RequestParam(defaultValue = "DRIVING") String travelMode
    ) {
        return ResponseEntity.ok(
                tripService.optimizeTripRoute(
                        activityId,
                        travelMode
                )
        );
    }

    @GetMapping("/{activityId}/external-route")
    public ResponseEntity<ExternalRouteResponse> getExternalRoute(
            @PathVariable String activityId,
            @RequestParam(defaultValue = "DRIVE")
            String travelMode
    ) {

        return ResponseEntity.ok(
                tripService.getExternalRoute(
                        activityId,
                        travelMode
                )
        );
    }

    @GetMapping("/{activityId}/stops/{stopId}/nearby")
    public ResponseEntity<List<NearbyPlaceResponse>> getNearbyPlaces(
            @PathVariable String activityId,
            @PathVariable Integer stopId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double radiusKm
    ) {

        return ResponseEntity.ok(
                tripService.getNearbyPlaces(
                        activityId,
                        stopId,
                        type,
                        radiusKm
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<TripSummaryResponse>> getMyTrips(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ActivityStatus status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean upcoming,
            @RequestParam(required = false) Boolean active
    ) {
        return ResponseEntity.ok(tripService.searchTrips(search, status, sortBy, direction, upcoming, active));
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<TripResponse> updateTrip(
            @PathVariable String activityId,
            @Valid @RequestBody UpdateTripRequest request
    ) {
        return ResponseEntity.ok(tripService.updateTrip(activityId, request));
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable String activityId) {
        tripService.deleteTrip(activityId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{activityId}/restore")
    public ResponseEntity<TripResponse> restoreTrip(@PathVariable String activityId) {
        return ResponseEntity.ok(tripService.restoreTrip(activityId));
    }

    @PatchMapping("/{activityId}/complete")
    public ResponseEntity<TripResponse> completeTrip(@PathVariable String activityId) {
        return ResponseEntity.ok(tripService.completeTrip(activityId));
    }
}
