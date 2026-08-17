package com.movem.backend.Dto.response.FitnessResponse.Club;

import com.movem.backend.model.enums.Collaboration.JoinRequestStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessClubJoinRequestResponse {

    private Long id;

    private Integer clubId;

    private Integer requesterId;

    private JoinRequestStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime respondedAt;
}