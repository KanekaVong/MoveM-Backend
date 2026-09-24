package com.movem.backend.fitness.challenges.repositories;

import com.movem.backend.fitness.challenges.entities.SoloChallenge;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoloChallengeCatalogRepository
        extends JpaRepository<SoloChallenge, Integer> {

    List<SoloChallenge> findByWorkoutType(
            WorkoutType workoutType
    );

}
