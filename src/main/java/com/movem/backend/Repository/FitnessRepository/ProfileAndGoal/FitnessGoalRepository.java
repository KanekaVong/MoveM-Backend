package com.movem.backend.Repository.FitnessRepository.ProfileAndGoal;

import com.movem.backend.Entity.Fitness.ProfileAndGoal.FitnessGoal;
import com.movem.backend.Entity.Auth.User;
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
