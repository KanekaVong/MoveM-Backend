package com.movem.backend.Service.FitnessServices.Challenge;

import com.movem.backend.Dto.response.FitnessResponse.Challenge.FitnessChallengeParticipantResponse;

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
