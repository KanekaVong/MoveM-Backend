package com.movem.backend.Dto.response.FitnessResponse.Club;

import com.movem.backend.model.enums.Fitness.ClubPrivacy;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessClubResponse {

    private Integer id;

    private String name;

    private String description;

    private Integer createdBy;

    private ClubPrivacy privacy;

    private String joinToken;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}