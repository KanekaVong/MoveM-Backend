package com.movem.backend.Service.FitnessServices.Challenge;

import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupFitnessChallengeFromCatalogRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupFitnessChallengeRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.UpdateGroupFitnessChallengeRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.GroupFitnessChallengeResponse;

import java.util.List;

public interface GroupFitnessChallengeService {

    GroupFitnessChallengeResponse createChallenge(
            Integer clubId,
            CreateGroupFitnessChallengeRequest request
    );

    GroupFitnessChallengeResponse getChallenge(
            Integer challengeId
    );

    List<GroupFitnessChallengeResponse> getClubChallenges(
            Integer clubId
    );

    List<GroupFitnessChallengeResponse> getMyCreatedChallenges();

    GroupFitnessChallengeResponse updateChallenge(
            Integer challengeId,
            UpdateGroupFitnessChallengeRequest request
    );

    GroupFitnessChallengeResponse createChallengeFromCatalog(
            Integer clubId,
            Integer catalogId,
            CreateGroupFitnessChallengeFromCatalogRequest request
    );

    void deleteChallenge(
            Integer challengeId
    );
}