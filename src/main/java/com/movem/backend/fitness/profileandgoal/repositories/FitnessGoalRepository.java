package com.movem.backend.fitness.profileandgoal.repositories;

import com.movem.backend.fitness.profileandgoal.entities.FitnessGoal;
import com.movem.backend.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessGoalRepository
        extends JpaRepository<FitnessGoal, Integer> {

    List<FitnessGoal> findByUserOrderByCreatedAtDesc(User user );

    Optional<FitnessGoal> findByIdAndUser (Integer id, User user);

    Optional<FitnessGoal> findFirstByUserAndStatusOrderByCreatedAtDesc(
            User user,
            String status
    );
}
