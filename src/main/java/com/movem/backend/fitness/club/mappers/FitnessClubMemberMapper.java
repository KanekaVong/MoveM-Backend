package com.movem.backend.fitness.club.mappers;

import com.movem.backend.fitness.club.dtos.responses.FitnessClubMemberResponse;
import com.movem.backend.fitness.club.entities.FitnessClubMember;
import org.springframework.stereotype.Component;

@Component
public class FitnessClubMemberMapper {

    public FitnessClubMemberResponse toResponse(
            FitnessClubMember member
    ) {

        return FitnessClubMemberResponse.builder()
                .clubId(
                        member.getFitnessClub() != null
                                ? member.getFitnessClub().getId()
                                : null
                )
                .userId(
                        member.getUser() != null
                                ? member.getUser().getId()
                                : null
                )
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .build();
    }
}