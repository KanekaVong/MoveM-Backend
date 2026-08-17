package com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFitnessChallengeParticipantRequest {

    @NotNull(message = "Challenge ID is required.")
    private Integer challengeId;
}