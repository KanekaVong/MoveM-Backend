package com.movem.backend.Service.FitnessServices.Workout;

import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutRoutePoint;

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