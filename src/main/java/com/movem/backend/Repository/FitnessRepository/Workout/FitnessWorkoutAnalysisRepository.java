package com.movem.backend.Repository.FitnessRepository.Workout;

import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutAnalysis;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FitnessWorkoutAnalysisRepository
        extends JpaRepository<FitnessWorkoutAnalysis, Integer> {

    Optional<FitnessWorkoutAnalysis> findByWorkoutSession(
            FitnessWorkoutSession workoutSession
    );
}