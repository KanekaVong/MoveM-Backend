package com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateGroupFitnessChallengeFromCatalogRequest {

    @NotNull(message = "Start time is required.")
    private LocalDateTime startAt;

    @NotNull(message = "End time is required.")
    private LocalDateTime endAt;
}