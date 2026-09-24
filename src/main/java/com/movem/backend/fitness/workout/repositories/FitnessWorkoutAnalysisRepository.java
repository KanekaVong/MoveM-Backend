package com.movem.backend.fitness.workout.repositories;

import com.movem.backend.fitness.workout.entities.FitnessWorkoutAnalysis;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FitnessWorkoutAnalysisRepository
        extends JpaRepository<FitnessWorkoutAnalysis, Integer> {

    Optional<FitnessWorkoutAnalysis> findByWorkoutSession(
            FitnessWorkoutSession workoutSession
    );
}