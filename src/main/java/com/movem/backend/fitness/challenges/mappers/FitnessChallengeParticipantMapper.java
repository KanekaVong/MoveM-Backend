package com.movem.backend.fitness.challenges.mappers;

import com.movem.backend.fitness.challenges.dtos.responses.FitnessChallengeParticipantResponse;
import com.movem.backend.fitness.challenges.entities.FitnessChallengeParticipant;
import org.springframework.stereotype.Component;

@Component
public class FitnessChallengeParticipantMapper {

    public FitnessChallengeParticipantResponse toResponse(
            FitnessChallengeParticipant participant
    ) {

        return FitnessChallengeParticipantResponse.builder()
                .id(participant.getId())

                .challengeId(
                        participant.getChallenge() != null
                                ? participant.getChallenge().getId()
                                : null
                )

                .userId(
                        participant.getUser() != null
                                ? participant.getUser().getId()
                                : null
                )

                .joinedAt(
                        participant.getJoinedAt()
                )

                .completedAt(
                        participant.getCompletedAt()
                )

                .status(
                        participant.getStatus()
                )

                .build();
    }
}