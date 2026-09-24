package com.movem.backend.fitness.challenges.services;

import com.movem.backend.fitness.challenges.dtos.responses.FitnessChallengeParticipantResponse;

import java.util.List;

public interface FitnessChallengeParticipantService {

    FitnessChallengeParticipantResponse joinChallenge(
            Integer challengeId
    );

    FitnessChallengeParticipantResponse getParticipant(
            Integer participantId
    );

    FitnessChallengeParticipantResponse getMyParticipation(
            Integer challengeId
    );

    List<FitnessChallengeParticipantResponse> getChallengeParticipants(
            Integer challengeId
    );

    List<FitnessChallengeParticipantResponse> getMyParticipations();

    void leaveChallenge(
            Integer challengeId
    );
}
