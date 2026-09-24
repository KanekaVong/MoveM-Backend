package com.movem.backend.fitness.workout.services;

import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import com.movem.backend.authentication.entities.User;

import java.math.BigDecimal;

public interface CalorieCalculationService {

    BigDecimal calculateCalories(
            User user,
            FitnessWorkoutSession session
    );
}