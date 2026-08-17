package com.movem.backend.Mapper.FitnessMapper.Club;

import com.movem.backend.Dto.response.FitnessResponse.Club.FitnessClubMemberResponse;
import com.movem.backend.Entity.Fitness.Club.FitnessClubMember;
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