package com.movem.backend.fitness.workout.controllers;

import com.movem.backend.fitness.workout.dtos.requests.FitnessWorkoutAnalysisRequest;
import com.movem.backend.fitness.workout.dtos.responses.FitnessWorkoutAnalysisResponse;
import com.movem.backend.fitness.workout.services.FitnessWorkoutAnalysisService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fitness/workouts")
@Tag(
        name = "Fitness - Workouts",
        description = "Analyzing Joints, Steps, or Calories"
)
@RequiredArgsConstructor
public class FitnessWorkoutAnalysisController {

    private final FitnessWorkoutAnalysisService analysisService;

    @PostMapping("/{sessionId}/analysis")
    public ResponseEntity<FitnessWorkoutAnalysisResponse> saveAnalysis(
            @PathVariable Integer sessionId,
            @Valid @RequestBody FitnessWorkoutAnalysisRequest request
    ) {
        return ResponseEntity.ok(
                analysisService.saveAnalysis(
                        sessionId,
                        request
                )
        );
    }

    @GetMapping("/{sessionId}/analysis")
    public ResponseEntity<FitnessWorkoutAnalysisResponse> getAnalysis(
            @PathVariable Integer sessionId
    ) {
        return ResponseEntity.ok(
                analysisService.getAnalysis(
                        sessionId
                )
        );
    }
}