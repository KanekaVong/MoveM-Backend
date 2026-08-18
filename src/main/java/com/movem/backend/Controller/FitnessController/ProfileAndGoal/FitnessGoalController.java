package com.movem.backend.Controller.FitnessController.ProfileAndGoal;

import com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal.CreateFitnessGoalRequest;
import com.movem.backend.Dto.response.FitnessResponse.ProfileAndGoal.FitnessGoalResponse;
import com.movem.backend.Service.FitnessServices.ProfileAndGoal.FitnessGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/goals")
@RequiredArgsConstructor
public class FitnessGoalController {

    private final FitnessGoalService fitnessGoalService;

    @PostMapping
    public ResponseEntity<FitnessGoalResponse> createGoal(
            @Valid @RequestBody CreateFitnessGoalRequest request
    ) {

        FitnessGoalResponse response =
                fitnessGoalService.createGoal(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<FitnessGoalResponse>> getMyGoals() {

        return ResponseEntity.ok(
                fitnessGoalService.getMyGoals()
        );
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<FitnessGoalResponse> getGoal(
            @PathVariable Integer goalId
    ) {

        return ResponseEntity.ok(
                fitnessGoalService.getGoal(goalId)
        );
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<FitnessGoalResponse> updateGoal(
            @PathVariable Integer goalId,
            @Valid @RequestBody CreateFitnessGoalRequest request
    ) {

        return ResponseEntity.ok(
                fitnessGoalService.updateGoal(
                        goalId,
                        request
                )
        );
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(
            @PathVariable Integer goalId
    ) {

        fitnessGoalService.deleteGoal(goalId);

        return ResponseEntity.noContent().build();
    }

}