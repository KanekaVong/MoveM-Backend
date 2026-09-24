package com.movem.backend.fitness.club.repositories;

import com.movem.backend.fitness.club.entities.FitnessClub;
import com.movem.backend.fitness.club.entities.FitnessClubJoinRequest;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.JoinRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FitnessClubJoinRequestRepository
        extends JpaRepository<FitnessClubJoinRequest, Long> {

    List<FitnessClubJoinRequest> findByFitnessClub(
            FitnessClub fitnessClub
    );

    List<FitnessClubJoinRequest> findByFitnessClubAndStatus(
            FitnessClub fitnessClub,
            JoinRequestStatus status
    );

    List<FitnessClubJoinRequest> findByRequester(
            User requester
    );

    Optional<FitnessClubJoinRequest> findByFitnessClubAndRequester(
            FitnessClub fitnessClub,
            User requester
    );

    Optional<FitnessClubJoinRequest>
    findByFitnessClubAndRequesterAndStatus(
            FitnessClub fitnessClub,
            User requester,
            JoinRequestStatus status
    );
}