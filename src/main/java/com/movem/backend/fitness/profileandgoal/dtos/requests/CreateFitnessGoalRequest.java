package com.movem.backend.fitness.profileandgoal.dtos.requests;


import com.movem.backend.commons.enums.Fitness.GoalType;
import com.movem.backend.commons.enums.Fitness.WorkoutLevel;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFitnessGoalRequest {
    @NotNull(message = "Goal Type is required.")
    GoalType goalType;

    @NotNull(message = "Target weight is required.")
    @DecimalMin(value = "1.0", message = "Target weight must be greater than 0.")
    BigDecimal targetWeight;

    @NotNull(message = "Target timeline is required.")
    @Future(message = "Target timeline must be in the future.")
    LocalDate targetTimeline;

    @NotNull(message = "Workout Level is required.")
    WorkoutLevel workoutLevel;

}
