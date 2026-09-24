package com.movem.backend.fitness.challenges.services;

import com.movem.backend.fitness.challenges.dtos.requests.GroupChallenge.CreateGroupFitnessChallengeFromCatalogRequest;
import com.movem.backend.fitness.challenges.dtos.requests.GroupChallenge.CreateGroupFitnessChallengeRequest;
import com.movem.backend.fitness.challenges.dtos.requests.GroupChallenge.UpdateGroupFitnessChallengeRequest;
import com.movem.backend.fitness.challenges.dtos.responses.GroupFitnessChallengeResponse;
import com.movem.backend.fitness.challenges.dtos.responses.SocialChallengeResponse;

import java.util.List;

public interface GroupFitnessChallengeService {
    GroupFitnessChallengeResponse createChallenge(Integer clubId, CreateGroupFitnessChallengeRequest request);
    GroupFitnessChallengeResponse getChallenge(Integer challengeId);
    List<GroupFitnessChallengeResponse> getClubChallenges(Integer clubId);
    List<GroupFitnessChallengeResponse> getMyCreatedChallenges();
    SocialChallengeResponse getSocialChallenge(Integer challengeId);
    GroupFitnessChallengeResponse updateChallenge(Integer challengeId, UpdateGroupFitnessChallengeRequest request);
    GroupFitnessChallengeResponse createChallengeFromCatalog(Integer clubId, Integer catalogId, CreateGroupFitnessChallengeFromCatalogRequest request);
    void deleteChallenge(Integer challengeId);
}