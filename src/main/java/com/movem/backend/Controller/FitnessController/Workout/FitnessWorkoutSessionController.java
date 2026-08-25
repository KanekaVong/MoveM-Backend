package com.movem.backend.Controller.FitnessController.Workout;

import com.movem.backend.Dto.request.FitnessRequest.Workout.ShareWorkoutRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.StartWorkoutRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutProgressRequest;
import com.movem.backend.Dto.request.FitnessRequest.Workout.WorkoutRoutePointsRequest;
import com.movem.backend.Dto.response.FitnessResponse.Social.SocialWorkoutResponse;
import com.movem.backend.Dto.response.FitnessResponse.Workout.*;
import com.movem.backend.Service.FitnessServices.Workout.FitnessWorkoutSessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/workouts")
@Tag(
        name = "Fitness - Workouts",
        description = "Workout tracking"
)
@RequiredArgsConstructor
public class FitnessWorkoutSessionController {

    private final FitnessWorkoutSessionService workoutSessionService;

    @PostMapping("/start")
    public ResponseEntity<FitnessWorkoutSessionResponse> startWorkout(
            @Valid @RequestBody StartWorkoutRequest request
    ) {
        return ResponseEntity.ok(
                workoutSessionService.startWorkout(request)
        );
    }

    @PatchMapping("/{sessionId}/progress")
    public ResponseEntity<FitnessWorkoutSessionResponse> updateProgress(
            @PathVariable Integer sessionId,
            @Valid @RequestBody WorkoutProgressRequest request
    ) {

        FitnessWorkoutSessionResponse response =
                workoutSessionService.updateProgress(
                        sessionId,
                        request
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{sessionId}/pause")
    public ResponseEntity<Void> pauseWorkout(
            @PathVariable Integer sessionId
    ) {

        workoutSessionService.pauseWorkout(
                sessionId
        );

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{sessionId}/resume")
    public ResponseEntity<Void> resumeWorkout(
            @PathVariable Integer sessionId
    ) {

        workoutSessionService.resumeWorkout(
                sessionId
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sessionId}/finish")
    public ResponseEntity<FitnessWorkoutSessionResponse> finishWorkout(
            @PathVariable Integer sessionId
    ) {

        FitnessWorkoutSessionResponse response =
                workoutSessionService.finishWorkout(sessionId);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<String> deleteWorkout(
            @PathVariable Integer sessionId
    ) {

        workoutSessionService.deleteWorkout(sessionId);

        return ResponseEntity.ok(
                "Workout deleted successfully."
        );
    }

    @GetMapping
    public ResponseEntity<List<FitnessWorkoutSessionResponse>> getMyWorkoutSessions() {

        List<FitnessWorkoutSessionResponse> response =
                workoutSessionService.getMySessions();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<WorkoutHistoryResponse>>
    getWorkoutHistory() {

        return ResponseEntity.ok(
                workoutSessionService.getWorkoutHistory()
        );
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<WorkoutDetailsResponse>
    getWorkoutDetails(
            @PathVariable Integer sessionId
    ) {

        return ResponseEntity.ok(
                workoutSessionService.getWorkoutDetails(
                        sessionId
                )
        );
    }

    //GPS ROUTE

    @PostMapping("/{sessionId}/route-points")
    public ResponseEntity<Void> addRoutePoints(
            @PathVariable Integer sessionId,
            @Valid @RequestBody WorkoutRoutePointsRequest request
    ) {

        workoutSessionService.addRoutePoints(
                sessionId,
                request
        );

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/social")
    public ResponseEntity<SocialWorkoutResponse> getSocialWorkout(
            @PathVariable Integer sessionId
    ) {
        return ResponseEntity.ok(
                workoutSessionService.getSocialWorkout(
                        sessionId
                )
        );
    }

    @GetMapping("/{sessionId}/summary")
    public ResponseEntity<FitnessWorkoutSummaryResponse> getWorkoutSummary(
            @PathVariable Integer sessionId
    ) {
        return ResponseEntity.ok(
                workoutSessionService.getWorkoutSummary(
                        sessionId
                )
        );
    }

    @PatchMapping("/{sessionId}/share")
    public ResponseEntity<Void> updateWorkoutSharing(
            @PathVariable Integer sessionId,
            @Valid @RequestBody ShareWorkoutRequest request
    ) {

        workoutSessionService.updateWorkoutSharing(
                sessionId,
                request
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/social-feed")
    public ResponseEntity<List<SharedWorkoutPostResponse>>
    getSocialWorkoutFeed() {

        return ResponseEntity.ok(
                workoutSessionService.getSocialWorkoutFeed()
        );
    }

}