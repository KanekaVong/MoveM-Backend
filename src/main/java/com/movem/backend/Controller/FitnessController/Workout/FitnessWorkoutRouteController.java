package com.movem.backend.Controller.FitnessController.Workout;

import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutRoutePointsRequest;
import com.movem.backend.Dto.response.FitnessResponse.Workout.FitnessWorkoutRoutePointResponse;
import com.movem.backend.Service.FitnessServices.Workout.FitnessWorkoutRouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/workouts")
@Tag(
        name = "Fitness - Workout Routes",
        description = "Workout Route tracking"
)

@RequiredArgsConstructor
public class FitnessWorkoutRouteController {

    private final FitnessWorkoutRouteService routeService;

    @PostMapping("/{sessionId}/route")
    public ResponseEntity<Void> addRoutePoints(
            @PathVariable Integer sessionId,
            @Valid @RequestBody WorkoutRoutePointsRequest request
    ) {

        routeService.addRoutePoints(
                sessionId,
                request
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/route")
    public ResponseEntity<List<FitnessWorkoutRoutePointResponse>> getRoute(
            @PathVariable Integer sessionId
    ) {

        return ResponseEntity.ok(
                routeService.getRoute(sessionId)
        );
    }
}