package com.movem.backend.fitness.club.dtos.requests;

import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFitnessClubMemberRoleRequest {

    @NotNull(message = "Club role is required.")
    private FitnessClubRole role;
}