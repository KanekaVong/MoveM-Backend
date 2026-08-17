package com.movem.backend.Dto.request.FitnessRequest.Club;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFitnessClubMemberRequest {

    @NotNull(message = "User ID is required.")
    private Integer userId;
}