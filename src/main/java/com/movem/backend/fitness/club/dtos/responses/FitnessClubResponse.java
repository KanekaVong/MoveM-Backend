package com.movem.backend.fitness.club.dtos.responses;

import com.movem.backend.commons.enums.Fitness.ClubPrivacy;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessClubResponse {
     Integer id;
     String name;
     String description;
     Integer createdBy;
     ClubPrivacy privacy;
     String profilePic;
     String coverPic;
     String joinToken;
     LocalDateTime createdAt;
     LocalDateTime updatedAt;
     Long memberCount;
}