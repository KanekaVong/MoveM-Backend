package com.movem.backend.fitness.workout.dtos.requests;

import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.commons.enums.Fitness.TrackingMode;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class FitnessWorkoutSearchRequest {

    private String search;

    private WorkoutType workoutType;

    private FitnessWorkoutStatus status;

    private TrackingMode trackingMode;

    private BigDecimal minDistance;
    private BigDecimal maxDistance;

    private BigDecimal minCalories;
    private BigDecimal maxCalories;

    private LocalDate startDate;
    private LocalDate endDate;

    private String sortBy;
    private String direction;
}
