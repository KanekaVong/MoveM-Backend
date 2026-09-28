package com.movem.backend.fitness.club.dtos.requests;

import com.movem.backend.commons.enums.Fitness.ClubPrivacy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateFitnessClubRequest {
    @NotBlank(message = "Club name is required.")
    @Size(max = 150, message = "Club name cannot exceed 150 characters.")
    String name;

    @Size(max = 1000, message = "Club description cannot exceed 1000 characters.")
    String description;

    @NotNull(message = "Club privacy is required.")
    ClubPrivacy privacy;
}