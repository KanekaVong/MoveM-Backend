package com.movem.backend.fitness.club.repositories;

import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Fitness.ClubPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessClubRepository extends JpaRepository<FitnessClub, Integer> {
    List<FitnessClub> findByCreatedBy(User user);
    Optional<FitnessClub> findByJoinToken(String joinToken);
    List<FitnessClub> findByPrivacy(ClubPrivacy privacy);
    boolean existsByJoinToken(String joinToken);
    List<FitnessClub> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}