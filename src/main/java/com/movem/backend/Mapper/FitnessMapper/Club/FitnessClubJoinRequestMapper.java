package com.movem.backend.Mapper.FitnessMapper.Club;

import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubJoinRequestResponse;
import com.movem.backend.Entity.Fitness.Club.FitnessClubJoinRequest;
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