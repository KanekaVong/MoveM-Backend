package com.movem.backend.Dto.request.FitnessRequest.Challenge.SoloChallenge;

import com.movem.backend.model.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.model.enums.Fitness.WorkoutLevel;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSoloChallengeRequest {

    @NotBlank(message = "Challenge name is required.")
    @Size(max = 150, message = "Challenge name cannot exceed 150 characters.")
    private String name;

    @NotNull(message = "Workout type is required.")
    private WorkoutType type;

    @NotNull(message = "Workout level is required.")
    private WorkoutLevel workoutLevel;

    @NotNull(message = "Target value is required.")
    @DecimalMin(
            value = "0.01",
            message = "Target value must be greater than 0."
    )
    private BigDecimal targetValue;

    @NotNull(message = "Target unit is required.")
    private ChallengeTargetUnit targetUnit;

    private String description;
}