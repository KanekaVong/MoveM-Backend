package com.movem.backend.fitness.club.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessClubRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessClubMemberResponse {
    Integer clubId;
    Integer userId;
    FitnessClubRole role;
    LocalDateTime joinedAt;
}