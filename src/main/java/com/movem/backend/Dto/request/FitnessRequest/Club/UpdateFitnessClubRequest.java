package com.movem.backend.Dto.request.FitnessRequest.Club;

import com.movem.backend.model.enums.Fitness.ClubPrivacy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFitnessClubRequest {

    @NotBlank(message = "Club name is required.")
    @Size(
            max = 150,
            message = "Club name cannot exceed 150 characters."
    )
    private String name;

    @Size(
            max = 1000,
            message = "Club description cannot exceed 1000 characters."
    )
    private String description;

    @NotNull(message = "Club privacy is required.")
    private ClubPrivacy privacy;
}