package com.movem.backend.Service.FitnessServices.Club;

import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubJoinRequestResponse;

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