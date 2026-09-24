package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessChallengeParticipantStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FitnessChallengeParticipantResponse {

    private Integer id;

    private Integer challengeId;

    private Integer userId;

    private LocalDateTime joinedAt;

    private LocalDateTime completedAt;

    private FitnessChallengeParticipantStatus status;
}