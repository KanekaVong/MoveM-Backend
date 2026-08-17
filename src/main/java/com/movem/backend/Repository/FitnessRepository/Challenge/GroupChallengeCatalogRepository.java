package com.movem.backend.Repository.FitnessRepository.Challenge;

import com.movem.backend.Entity.Fitness.Challenge.GroupChallengeCatalog;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupChallengeCatalogRepository
        extends JpaRepository<GroupChallengeCatalog, Integer> {

    List<GroupChallengeCatalog> findByWorkoutType(
            WorkoutType workoutType
    );
}