package com.movem.backend.fitness.challenges.mappers;

import com.movem.backend.fitness.challenges.dtos.responses.GroupChallengeCatalogResponse;
import com.movem.backend.fitness.challenges.entities.GroupChallengeCatalog;
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