package com.movem.backend.Mapper.FitnessMapper.Challenge;

import com.movem.backend.Dto.response.FitnessResponse.Challenge.FitnessChallengeParticipantResponse;
import com.movem.backend.Entity.Fitness.Challenge.FitnessChallengeParticipant;
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