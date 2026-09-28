package com.movem.backend.fitness.workout.dtos.requests;

import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.commons.enums.Fitness.TrackingMode;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessWorkoutSearchRequest {
    String search;

    WorkoutType workoutType;

    FitnessWorkoutStatus status;

    TrackingMode trackingMode;

    BigDecimal minDistance;
    BigDecimal maxDistance;

    BigDecimal minCalories;
    BigDecimal maxCalories;

    LocalDate startDate;
    LocalDate endDate;

    String sortBy;
    String direction;
}
