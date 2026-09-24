package com.movem.backend.fitness.challenges.services;

import com.movem.backend.fitness.challenges.dtos.requests.SoloChallenge.CreateSoloChallengeRequest;
import com.movem.backend.fitness.challenges.dtos.requests.SoloChallenge.UpdateSoloChallengeRequest;
import com.movem.backend.fitness.challenges.dtos.responses.SoloChallengeResponse;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
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