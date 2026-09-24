package com.movem.backend.fitness.club.mappers;

import com.movem.backend.fitness.club.dtos.responses.FitnessClubJoinRequestResponse;
import com.movem.backend.fitness.club.entities.FitnessClubJoinRequest;
import org.springframework.stereotype.Component;

@Component
public class FitnessClubJoinRequestMapper {

    public FitnessClubJoinRequestResponse toResponse(
            FitnessClubJoinRequest request
    ) {

        return FitnessClubJoinRequestResponse.builder()
                .id(request.getId())
                .clubId(
                        request.getFitnessClub() != null
                                ? request.getFitnessClub().getId()
                                : null
                )
                .requesterId(
                        request.getRequester() != null
                                ? request.getRequester().getId()
                                : null
                )
                .status(request.getStatus())
                .requestedAt(request.getRequestedAt())
                .respondedAt(request.getRespondedAt())
                .build();
    }
}