package com.movem.backend.fitness.club.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddFitnessClubMemberRequest {
    @NotNull(message = "User ID is required.")
    Integer userId;
}