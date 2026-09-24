package com.movem.backend.fitness.club.dtos.responses;

import com.movem.backend.commons.enums.shared.JoinRequestStatus;
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