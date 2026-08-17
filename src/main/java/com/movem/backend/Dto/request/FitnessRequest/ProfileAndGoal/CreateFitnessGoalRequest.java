package com.movem.backend.Dto.request.FitnessRequest.ProfileAndGoal;


import com.movem.backend.model.enums.Fitness.GoalType;
import com.movem.backend.model.enums.Fitness.WorkoutLevel;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateFitnessGoalRequest {

    @NotNull(message = "Goal Type is required.")
    private GoalType goalType;

    @NotNull(message = "Target weight is required.")
    @DecimalMin(
            value = "1.0",
            message = "Target weight must be greater than 0."
    )
    private BigDecimal targetWeight;

    @NotNull(message = "Target timeline is required.")
    @Future(message = "Target timeline must be in the future.")
    private LocalDate targetTimeline;

    @NotNull(message = "Workout Level is required.")
    private WorkoutLevel workoutLevel;

}
