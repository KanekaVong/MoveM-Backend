package com.movem.backend.Service.FitnessServices.Workout;

import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import com.movem.backend.Entity.Auth.User;

import java.math.BigDecimal;

public interface CalorieCalculationService {

    BigDecimal calculateCalories(
            User user,
            FitnessWorkoutSession session
    );
}