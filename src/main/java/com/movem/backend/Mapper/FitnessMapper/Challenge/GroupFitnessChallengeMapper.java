package com.movem.backend.Mapper.FitnessMapper.Challenge;

import com.movem.backend.Dto.response.FitnessResponse.Challenge.GroupFitnessChallengeResponse;
import com.movem.backend.Entity.Fitness.Challenge.GroupFitnessChallenge;
import org.springframework.stereotype.Component;

@Component
public class GroupFitnessChallengeMapper {

    public GroupFitnessChallengeResponse toResponse(
            GroupFitnessChallenge challenge
    ) {

        return GroupFitnessChallengeResponse.builder()
                .id(challenge.getId())

                .clubId(
                        challenge.getFitnessClub() != null
                                ? challenge.getFitnessClub().getId()
                                : null
                )

                .createdBy(
                        challenge.getCreatedBy() != null
                                ? challenge.getCreatedBy().getId()
                                : null
                )

                .name(challenge.getName())

                .workoutType(
                        challenge.getWorkoutType()
                )

                .targetValue(
                        challenge.getTargetValue()
                )

                .targetUnit(
                        challenge.getTargetUnit()
                )

                .description(
                        challenge.getDescription()
                )

                .catalogId(
                        challenge.getCatalog() != null
                                ? challenge.getCatalog().getId()
                                : null
                )

                .challengeSource(
                        challenge.getChallengeSource()
                )

                .startAt(
                        challenge.getStartAt()
                )

                .endAt(
                        challenge.getEndAt()
                )

                .status(
                        challenge.getStatus()
                )

                .createdAt(
                        challenge.getCreatedAt()
                )

                .updatedAt(
                        challenge.getUpdatedAt()
                )

                .build();
    }
}