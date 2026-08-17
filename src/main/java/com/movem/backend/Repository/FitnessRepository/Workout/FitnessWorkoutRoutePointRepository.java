package com.movem.backend.Repository.FitnessRepository.Workout;

import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutRoutePoint;
import com.movem.backend.Entity.Fitness.WorkoutSession.FitnessWorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FitnessWorkoutRoutePointRepository
        extends JpaRepository<FitnessWorkoutRoutePoint, Long> {

    List<FitnessWorkoutRoutePoint>
    findByWorkoutSessionOrderByPointSequenceAsc(
            FitnessWorkoutSession workoutSession
    );
}