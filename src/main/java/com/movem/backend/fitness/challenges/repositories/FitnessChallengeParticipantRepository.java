package com.movem.backend.fitness.challenges.repositories;

import com.movem.backend.fitness.challenges.entities.FitnessChallengeParticipant;
import com.movem.backend.fitness.challenges.entities.GroupFitnessChallenge;
import com.movem.backend.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessChallengeParticipantRepository
        extends JpaRepository<FitnessChallengeParticipant, Integer> {

    List<FitnessChallengeParticipant> findByChallenge(
            GroupFitnessChallenge challenge
    );

    List<FitnessChallengeParticipant> findByUser(
            User user
    );

    Optional<FitnessChallengeParticipant>
    findByChallengeAndUser(
            GroupFitnessChallenge challenge,
            User user
    );

    long countByChallenge(GroupFitnessChallenge challenge);
}