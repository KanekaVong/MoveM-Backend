package com.movem.backend.Dto.response.FitnessResponse.Challenge;

import com.movem.backend.model.enums.Fitness.FitnessChallengeParticipantStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
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