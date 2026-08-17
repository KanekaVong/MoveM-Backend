package com.movem.backend.Repository.FitnessRepository.Challenge;

import com.movem.backend.Entity.Fitness.Challenge.SoloChallenge;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SoloChallengeCatalogRepository
        extends JpaRepository<SoloChallenge, Integer> {

    List<SoloChallenge> findByWorkoutType(
            WorkoutType workoutType
    );

}
