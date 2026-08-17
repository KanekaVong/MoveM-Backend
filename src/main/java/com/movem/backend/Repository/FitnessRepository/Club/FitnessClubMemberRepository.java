package com.movem.backend.Repository.FitnessRepository.Club;

import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMember;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMemberId;
import com.movem.backend.Entity.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessClubMemberRepository
        extends JpaRepository<FitnessClubMember, FitnessClubMemberId> {

    List<FitnessClubMember> findByFitnessClub(
            FitnessClub fitnessClub
    );

    List<FitnessClubMember> findByUser(
            User user
    );

    Optional<FitnessClubMember> findByFitnessClubAndUser(
            FitnessClub fitnessClub,
            User user
    );

    boolean existsByFitnessClubAndUser(
            FitnessClub fitnessClub,
            User user
    );

    long countByFitnessClub(
            FitnessClub fitnessClub
    );
}