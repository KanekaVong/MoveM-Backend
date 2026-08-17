package com.movem.backend.Mapper.FitnessMapper.Club;

import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubResponse;
import com.movem.backend.Entity.Fitness.Club.FitnessClub;
import org.springframework.stereotype.Component;

@Component
public class FitnessClubMapper {

    public FitnessClubResponse toResponse(
            FitnessClub club
    ) {

        return FitnessClubResponse.builder()
                .id(club.getId())
                .name(club.getName())
                .description(club.getDescription())
                .createdBy(
                        club.getCreatedBy() != null
                                ? club.getCreatedBy().getId()
                                : null
                )
                .privacy(club.getPrivacy())
                .joinToken(club.getJoinToken())
                .createdAt(club.getCreatedAt())
                .updatedAt(club.getUpdatedAt())
                .build();
    }
}
