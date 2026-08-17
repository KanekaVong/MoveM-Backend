package com.movem.backend.Mapper.FitnessMapper.Challenge;

import com.movem.backend.Dto.response.FitnessResponse.Challenge.GroupChallengeCatalogResponse;
import com.movem.backend.Entity.Fitness.Challenge.GroupChallengeCatalog;
import org.springframework.stereotype.Component;

@Component
public class GroupChallengeCatalogMapper {

    public GroupChallengeCatalogResponse toResponse(
            GroupChallengeCatalog catalog
    ) {

        return GroupChallengeCatalogResponse.builder()
                .id(catalog.getId())
                .name(catalog.getName())
                .workoutType(catalog.getWorkoutType())
                .targetValue(catalog.getTargetValue())
                .targetUnit(catalog.getTargetUnit())
                .description(catalog.getDescription())
                .createdAt(catalog.getCreatedAt())
                .updatedAt(catalog.getUpdatedAt())
                .build();
    }
}