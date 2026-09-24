package com.movem.backend.trip.controllers;


import com.movem.backend.trip.dtos.requests.Create.CreateTripStopRequest;
import com.movem.backend.trip.dtos.requests.Update.ReorderTripStopsRequest;
import com.movem.backend.trip.dtos.requests.Update.UpdateTripStopRequest;
import com.movem.backend.trip.dtos.responses.TripRoute.TripDirectionsResponse;
import com.movem.backend.trip.dtos.responses.TripStopResponse;
import com.movem.backend.trip.services.TripStopService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{activityId}/stops")
@Tag( name = "Trip - Trip Stop",
        description = "Create trip, add collaborators, plan trips seamlessly")
@RequiredArgsConstructor
public class TripStopController {

    private final TripStopService tripStopService;

    @PostMapping
    public ResponseEntity<TripStopResponse> addStop(
            @PathVariable String activityId,
            @Valid @RequestBody CreateTripStopRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripStopService.addStop(activityId, request));
    }

    @GetMapping
    public ResponseEntity<List<TripStopResponse>> getStops(@PathVariable String activityId) {
        return ResponseEntity.ok(tripStopService.getStops(activityId));
    }

    @PutMapping("/{stopId}")
    public ResponseEntity<TripStopResponse> updateStop(
            @PathVariable String activityId,
            @PathVariable Integer stopId,
            @Valid @RequestBody UpdateTripStopRequest request
    ) {
        return ResponseEntity.ok(tripStopService.updateStop(activityId, stopId, request));
    }

    @DeleteMapping("/{stopId}")
    public ResponseEntity<Void> removeStop(@PathVariable String activityId, @PathVariable Integer stopId) {
        tripStopService.removeStop(activityId, stopId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{tripActivityId}/stops/{stopId}/complete")
    public ResponseEntity<TripStopResponse> completeStop(
            @PathVariable String tripActivityId,
            @PathVariable Integer stopId
    ) {
        return ResponseEntity.ok(
                tripStopService.completeStop(
                        tripActivityId,
                        stopId
                )
        );
    }

    @PutMapping("/reorder")
    public ResponseEntity<List<TripStopResponse>> reorderStops(
            @PathVariable String activityId,
            @Valid @RequestBody ReorderTripStopsRequest request
    ) {
        return ResponseEntity.ok(tripStopService.reorderStops(activityId, request));
    }

    @GetMapping("/{stopId}/directions")
    public ResponseEntity<TripDirectionsResponse> getDirections(
            @PathVariable String activityId,
            @PathVariable Integer stopId
    ) {
        return ResponseEntity.ok(tripStopService.getDirections(activityId, stopId));
    }
}
