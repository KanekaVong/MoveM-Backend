package com.movem.backend.fitness.challenges.dtos.requests.GroupChallenge;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFitnessChallengeParticipantRequest {
    @NotNull(message = "Challenge ID is required.")
    Integer challengeId;
}