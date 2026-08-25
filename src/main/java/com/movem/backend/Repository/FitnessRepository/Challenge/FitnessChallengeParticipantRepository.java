package com.movem.backend.Repository.FitnessRepository.Challenge;

import com.movem.backend.Entity.Fitness.Challenge.FitnessChallengeParticipant;
import com.movem.backend.Entity.Fitness.Challenge.GroupFitnessChallenge;
import com.movem.backend.Entity.Auth.User;
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