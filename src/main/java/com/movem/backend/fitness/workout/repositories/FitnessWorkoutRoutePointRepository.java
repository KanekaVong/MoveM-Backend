package com.movem.backend.fitness.workout.repositories;

import com.movem.backend.fitness.workout.entities.FitnessWorkoutRoutePoint;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FitnessWorkoutRoutePointRepository
        extends JpaRepository<FitnessWorkoutRoutePoint, Long> {

    List<FitnessWorkoutRoutePoint>
    findByWorkoutSessionOrderByPointSequenceAsc(
            FitnessWorkoutSession workoutSession
    );
}