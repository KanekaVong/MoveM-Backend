package com.movem.backend.Service.FitnessServices.Challenge;


import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.CreateGroupChallengeCatalogRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.GroupChallenge.UpdateGroupChallengeCatalogRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.GroupChallengeCatalogResponse;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import org.hibernate.jdbc.Work;

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