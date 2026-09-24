package com.movem.backend.fitness.challenges.services;


import com.movem.backend.fitness.challenges.dtos.requests.GroupChallenge.CreateGroupChallengeCatalogRequest;
import com.movem.backend.fitness.challenges.dtos.requests.GroupChallenge.UpdateGroupChallengeCatalogRequest;
import com.movem.backend.fitness.challenges.dtos.responses.GroupChallengeCatalogResponse;
import com.movem.backend.commons.enums.Fitness.WorkoutType;

import java.util.List;

public interface GroupChallengeCatalogService {

    GroupChallengeCatalogResponse createCatalogChallenge(
            CreateGroupChallengeCatalogRequest request
    );

    GroupChallengeCatalogResponse getCatalogChallenge(
            Integer catalogId
    );

    List<GroupChallengeCatalogResponse> getAllCatalogChallenges();

    List<GroupChallengeCatalogResponse> getCatalogChallengesByWorkoutType(
            WorkoutType workoutType
    );

    GroupChallengeCatalogResponse updateCatalogChallenge(
            Integer catalogId,
            UpdateGroupChallengeCatalogRequest request
    );

    void deleteCatalogChallenge(
            Integer catalogId
    );
}