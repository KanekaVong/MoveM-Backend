package com.movem.backend.Repository.FitnessRepository.Club;

import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Fitness.ClubPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessClubRepository
        extends JpaRepository<FitnessClub, Integer> {

    List<FitnessClub> findByCreatedBy(
            User user
    );

    Optional<FitnessClub> findByJoinToken(
            String joinToken
    );

    List<FitnessClub> findByPrivacy(
            ClubPrivacy privacy
    );

    boolean existsByJoinToken(
            String joinToken
    );

    List<FitnessClub> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String name,
            String description
    );
}