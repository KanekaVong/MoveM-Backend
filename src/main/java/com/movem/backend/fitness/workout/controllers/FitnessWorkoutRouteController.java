package com.movem.backend.fitness.workout.controllers;

import com.movem.backend.fitness.workout.dtos.requests.WorkoutRoutePointsRequest;
import com.movem.backend.fitness.workout.dtos.responses.FitnessWorkoutRoutePointResponse;
import com.movem.backend.fitness.workout.services.FitnessWorkoutRouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/workouts")
@Tag(name = "Fitness - Workout Routes", description = "Workout Route tracking")
@RequiredArgsConstructor
public class FitnessWorkoutRouteController {
    private final FitnessWorkoutRouteService routeService;

    @PostMapping("/{sessionId}/route-points")
    public ResponseEntity<Void> addRoutePoints(@PathVariable Integer sessionId, @Valid @RequestBody WorkoutRoutePointsRequest request) {
        routeService.addRoutePoints(sessionId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/route")
    public ResponseEntity<List<FitnessWorkoutRoutePointResponse>> getRoute(@PathVariable Integer sessionId) {
        return ResponseEntity.ok(routeService.getRoute(sessionId));
    }
}