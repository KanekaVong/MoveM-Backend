package com.movem.backend.Controller.FitnessController.Challenge;

import com.movem.backend.Dto.request.FitnessRequest.Challenge.SoloChallenge.CreateSoloChallengeRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.SoloChallenge.UpdateSoloChallengeRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.SoloChallengeResponse;
import com.movem.backend.Service.FitnessServices.Challenge.SoloChallengeService;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/solo-challenges")
@Tag(
        name = "Fitness - Challenges",
        description = "Solo Challenges"
)
@RequiredArgsConstructor
public class SoloChallengeController {

    private final SoloChallengeService soloChallengeService;

    @GetMapping
    public ResponseEntity<List<SoloChallengeResponse>>
    getAllChallenges() {

        return ResponseEntity.ok(
                soloChallengeService.getAllChallenges()
        );
    }

    @GetMapping("/{challengeId}")
    public ResponseEntity<SoloChallengeResponse>
    getChallenge(
            @PathVariable Integer challengeId
    ) {

        return ResponseEntity.ok(
                soloChallengeService.getChallenge(
                        challengeId
                )
        );
    }

    @GetMapping("/type/{workoutType}")
    public ResponseEntity<List<SoloChallengeResponse>>
    getChallengesByType(
            @PathVariable WorkoutType workoutType
    ) {

        return ResponseEntity.ok(
                soloChallengeService.getChallengesByWorkoutType(
                        workoutType
                )
        );
    }

    @PostMapping
    public ResponseEntity<SoloChallengeResponse>
    createChallenge(
            @Valid @RequestBody
            CreateSoloChallengeRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        soloChallengeService
                                .createChallenge(request)
                );
    }

    @PutMapping("/{challengeId}")
    public ResponseEntity<SoloChallengeResponse>
    updateChallenge(
            @PathVariable Integer challengeId,

            @Valid @RequestBody
            UpdateSoloChallengeRequest request
    ) {

        return ResponseEntity.ok(
                soloChallengeService.updateChallenge(
                        challengeId,
                        request
                )
        );
    }

    @DeleteMapping("/{challengeId}")
    public ResponseEntity<Void>
    deleteChallenge(
            @PathVariable Integer challengeId
    ) {

        soloChallengeService.deleteChallenge(
                challengeId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}