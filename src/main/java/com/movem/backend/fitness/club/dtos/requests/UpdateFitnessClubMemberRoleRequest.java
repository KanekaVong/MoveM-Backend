package com.movem.backend.fitness.club.dtos.requests;

import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFitnessClubMemberRoleRequest {

    @NotNull(message = "Club role is required.")
    FitnessClubRole role;
}