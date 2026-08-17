package com.movem.backend.Repository.FitnessRepository.Club;

import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import com.movem.backend.Entity.Fitness.Club.FitnessClubJoinRequest;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Collaboration.JoinRequestStatus;
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