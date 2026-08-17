package com.movem.backend.Repository.FitnessRepository.ProfileAndGoal;

import com.movem.backend.Entity.Fitness.ProfileAndGoal.FitnessProfile;
import com.movem.backend.Entity.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FitnessProfileRepository
        extends JpaRepository<FitnessProfile, Integer> {

    Optional<FitnessProfile> findByUser(User currentUser);
}
