package com.movem.backend.fitness.club.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
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