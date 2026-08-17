package com.movem.backend.Service.FitnessServices.Challenge;

import com.movem.backend.Dto.request.FitnessRequest.Challenge.SoloChallenge.CreateSoloChallengeRequest;
import com.movem.backend.Dto.request.FitnessRequest.Challenge.SoloChallenge.UpdateSoloChallengeRequest;
import com.movem.backend.Dto.response.FitnessResponse.Challenge.SoloChallengeResponse;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import java.util.List;

public interface SoloChallengeService {

    List<SoloChallengeResponse> getAllChallenges();

    SoloChallengeResponse getChallenge(
            Integer challengeId
    );

    List<SoloChallengeResponse> getChallengesByWorkoutType(
            WorkoutType workoutType
    );

    SoloChallengeResponse createChallenge(
            CreateSoloChallengeRequest request
    );

    SoloChallengeResponse updateChallenge(
            Integer challengeId,
            UpdateSoloChallengeRequest request
    );

    void deleteChallenge(
            Integer challengeId
    );
}