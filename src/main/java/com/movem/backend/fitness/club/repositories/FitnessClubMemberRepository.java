package com.movem.backend.fitness.club.repositories;

import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.fitness.club.entities.FitnessClubMember;
import com.movem.backend.fitness.club.entities.FitnessClubMemberId;
import com.movem.backend.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessClubMemberRepository extends JpaRepository<FitnessClubMember, FitnessClubMemberId> {
    List<FitnessClubMember> findByFitnessClub(FitnessClub fitnessClub);
    List<FitnessClubMember> findByUser(User user);

    Optional<FitnessClubMember> findByFitnessClubAndUser(FitnessClub fitnessClub, User user);

    boolean existsByFitnessClubAndUser(FitnessClub fitnessClub, User user);

    long countByFitnessClub(FitnessClub fitnessClub);
}