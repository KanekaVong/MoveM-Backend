package com.movem.backend.fitness.club.dtos.responses;

import com.movem.backend.commons.enums.shared.JoinRequestStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessClubJoinRequestResponse {
    Long id;
    Integer clubId;
    Integer requesterId;
    JoinRequestStatus status;
    LocalDateTime requestedAt;
    LocalDateTime respondedAt;
}