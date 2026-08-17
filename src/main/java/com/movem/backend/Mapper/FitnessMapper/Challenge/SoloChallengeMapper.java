package com.movem.backend.Mapper.FitnessMapper.Challenge;

import com.movem.backend.Dto.response.FitnessResponse.Challenge.SoloChallengeResponse;
import com.movem.backend.Entity.Fitness.Challenge.SoloChallenge;
import org.springframework.stereotype.Component;

@Component
public class SoloChallengeMapper {

    public SoloChallengeResponse toResponse(
            SoloChallenge challenge
    ) {

        return SoloChallengeResponse.builder()
                .id(challenge.getId())
                .name(challenge.getName())
                .type(challenge.getWorkoutType())
                .workoutLevel(challenge.getWorkoutLevel())
                .targetValue(challenge.getTargetValue())
                .targetUnit(challenge.getTargetUnit())
                .calories(challenge.getCalories())
                .description(challenge.getDescription())
                .createdAt(challenge.getCreatedAt())
                .updatedAt(challenge.getUpdatedAt())
                .build();
    }
}