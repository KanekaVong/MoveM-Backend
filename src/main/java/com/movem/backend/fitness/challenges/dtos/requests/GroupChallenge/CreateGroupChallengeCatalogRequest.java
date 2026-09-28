package com.movem.backend.fitness.challenges.dtos.requests.GroupChallenge;

import com.movem.backend.commons.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateGroupChallengeCatalogRequest {

    @NotBlank(message = "Challenge name is required.")
    @Size(max = 150, message = "Challenge name cannot exceed 150 characters.")
    String name;

    @NotNull(message = "Workout type is required.")
    WorkoutType workoutType;

    @NotNull(message = "Target value is required.")
    @DecimalMin(value = "0.01", message = "Target value must be greater than 0.")
    BigDecimal targetValue;

    @NotNull(message = "Target unit is required.")
    ChallengeTargetUnit targetUnit;
    String description;
}