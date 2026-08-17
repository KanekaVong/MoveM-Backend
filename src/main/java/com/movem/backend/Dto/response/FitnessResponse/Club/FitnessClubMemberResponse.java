package com.movem.backend.Dto.response.FitnessResponse.Club;

import com.movem.backend.model.enums.Fitness.FitnessClubRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessClubMemberResponse {

    private Integer clubId;

    private Integer userId;

    private FitnessClubRole role;

    private LocalDateTime joinedAt;
}