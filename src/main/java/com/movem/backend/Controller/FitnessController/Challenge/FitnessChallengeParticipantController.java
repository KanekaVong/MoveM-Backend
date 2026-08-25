package com.movem.backend.Controller.FitnessController.Challenge;

import com.movem.backend.Dto.response.FitnessResponse.Challenge.FitnessChallengeParticipantResponse;
import com.movem.backend.Service.FitnessServices.Challenge.FitnessChallengeParticipantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fitness/group-challenges")
@Tag(
        name = "Fitness - Participant",
        description = "Participate in Solo or Group Challenges"
)
@RequiredArgsConstructor
public class FitnessChallengeParticipantController {

    private final FitnessChallengeParticipantService participantService;

    @PostMapping("/{challengeId}/join")
    public ResponseEntity<FitnessChallengeParticipantResponse>
    joinChallenge(
            @PathVariable Integer challengeId
    ) {

        return ResponseEntity.ok(
                participantService.joinChallenge(
                        challengeId
                )
        );
    }

    @GetMapping("/{challengeId}/participants/me")
    public ResponseEntity<FitnessChallengeParticipantResponse>
    getMyParticipation(
            @PathVariable Integer challengeId
    ) {

        return ResponseEntity.ok(
                participantService.getMyParticipation(
                        challengeId
                )
        );
    }

    @GetMapping("/{challengeId}/participants")
    public ResponseEntity<List<FitnessChallengeParticipantResponse>>
    getChallengeParticipants(
            @PathVariable Integer challengeId
    ) {

        return ResponseEntity.ok(
                participantService.getChallengeParticipants(
                        challengeId
                )
        );
    }

    @GetMapping("/participants/me")
    public ResponseEntity<List<FitnessChallengeParticipantResponse>>
    getMyParticipation() {

        return ResponseEntity.ok(
                participantService.getMyParticipations()
        );
    }

    @GetMapping("/participants/{participantId}")
    public ResponseEntity<FitnessChallengeParticipantResponse>
    getParticipant(
            @PathVariable Integer participantId
    ) {

        return ResponseEntity.ok(
                participantService.getParticipant(
                        participantId
                )
        );
    }

    @DeleteMapping("/{challengeId}/leave")
    public ResponseEntity<Void>
    leaveChallenge(
            @PathVariable Integer challengeId
    ) {

        participantService.leaveChallenge(
                challengeId
        );

        return ResponseEntity.noContent().build();
    }

}