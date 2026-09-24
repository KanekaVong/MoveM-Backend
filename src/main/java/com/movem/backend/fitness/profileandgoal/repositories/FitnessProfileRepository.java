package com.movem.backend.fitness.profileandgoal.repositories;

import com.movem.backend.fitness.profileandgoal.entities.FitnessProfile;
import com.movem.backend.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FitnessProfileRepository extends JpaRepository<FitnessProfile, Integer> {
    Optional<FitnessProfile> findByUser(User currentUser);
}
