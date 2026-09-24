package com.movem.backend.fitness.workout.services;

import com.movem.backend.fitness.workout.entities.FitnessWorkoutRoutePoint;

import java.math.BigDecimal;
import java.util.List;

public interface WorkoutRouteCalculationService {

    BigDecimal calculateDistance(
            List<FitnessWorkoutRoutePoint> points
    );

    BigDecimal calculateSpeed(
            BigDecimal distanceKm,
            Integer durationSeconds
    );

    BigDecimal calculatePace(
            BigDecimal distanceKm,
            Integer durationSeconds
    );
}