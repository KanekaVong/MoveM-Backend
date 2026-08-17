package com.movem.backend.Dto.request.FitnessRequest.Club;

import com.movem.backend.model.enums.Fitness.FitnessClubRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFitnessClubMemberRoleRequest {

    @NotNull(message = "Club role is required.")
    private FitnessClubRole role;
}