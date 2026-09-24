package com.movem.backend.fitness.challenges.repositories;

import com.movem.backend.fitness.challenges.entities.GroupChallengeCatalog;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupChallengeCatalogRepository
        extends JpaRepository<GroupChallengeCatalog, Integer> {

    List<GroupChallengeCatalog> findByWorkoutType(
            WorkoutType workoutType
    );
}