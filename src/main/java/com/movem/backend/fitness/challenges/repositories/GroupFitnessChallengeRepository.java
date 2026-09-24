package com.movem.backend.fitness.challenges.repositories;

import com.movem.backend.fitness.challenges.entities.GroupFitnessChallenge;
import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupFitnessChallengeRepository
        extends JpaRepository<GroupFitnessChallenge, Integer> {

    List<GroupFitnessChallenge>
    findByFitnessClubOrderByCreatedAtDesc(
            FitnessClub fitnessClub
    );

    List<GroupFitnessChallenge>
    findByCreatedByOrderByCreatedAtDesc(
            User user
    );

    Optional<GroupFitnessChallenge>
    findByIdAndCreatedBy(
            Integer challengeId,
            User user
    );
}