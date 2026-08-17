package com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge;

import com.movem.backend.model.enums.Fitness.ChallengeTargetUnit;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateGroupFitnessChallengeRequest {

    @NotBlank(message = "Challenge name is required.")
    @Size(
            max = 150,
            message = "Challenge name cannot exceed 150 characters."
    )
    private String name;

    @NotNull(message = "Workout type is required.")
    private WorkoutType workoutType;

    @NotNull(message = "Target value is required.")
    @DecimalMin(
            value = "0.01",
            message = "Target value must be greater than 0."
    )
    private BigDecimal targetValue;

    @NotNull(message = "Target unit is required.")
    private ChallengeTargetUnit targetUnit;

    private String description;

    @NotNull(message = "Start time is required.")
    private LocalDateTime startAt;

    @NotNull(message = "End time is required.")
    private LocalDateTime endAt;
}