package com.movem.backend.fitness.challenges.dtos.requests.GroupChallenge;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateGroupFitnessChallengeFromCatalogRequest {
    @NotNull(message = "Start time is required.")
    LocalDateTime startAt;
    @NotNull(message = "End time is required.")
    LocalDateTime endAt;
}