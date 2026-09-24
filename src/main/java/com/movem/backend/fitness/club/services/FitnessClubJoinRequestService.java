package com.movem.backend.fitness.club.services;

import com.movem.backend.fitness.club.dtos.responses.FitnessClubJoinRequestResponse;

import java.util.List;

public interface FitnessClubJoinRequestService {

    FitnessClubJoinRequestResponse requestToJoin(
            Integer clubId
    );

    List<FitnessClubJoinRequestResponse> getPendingRequests(
            Integer clubId
    );

    FitnessClubJoinRequestResponse approveRequest(
            Integer clubId,
            Long requestId
    );

    FitnessClubJoinRequestResponse rejectRequest(
            Integer clubId,
            Long requestId
    );

    void cancelRequest(
            Long requestId
    );

    List<FitnessClubJoinRequestResponse> getMyRequests();
}