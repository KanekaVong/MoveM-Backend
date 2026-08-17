package com.movem.backend.Repository.FitnessRepository.Challenge;

import com.movem.backend.Entity.Fitness.Challenge.GroupFitnessChallenge;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Auth.User;
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