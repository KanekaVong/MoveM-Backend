package com.movem.backend.fitness.club.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFitnessClubMemberRequest {

    @NotNull(message = "User ID is required.")
    private Integer userId;
}