package com.movem.backend.fitness.challenges.mappers;

import com.movem.backend.fitness.challenges.dtos.responses.SoloChallengeResponse;
import com.movem.backend.fitness.challenges.entities.SoloChallenge;
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