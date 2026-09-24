package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessChallengeStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SocialChallengeResponse {
    private Integer challengeId;
    private String name;
    private String description;
    private String workoutType;
    private BigDecimal targetValue;
    private String targetUnit;
    private FitnessChallengeStatus status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;

    private Integer creatorId;
    private String creatorUsername;

    private long participantCount;
    private long completedParticipants;

    private BigDecimal myProgress;
    private boolean myCompleted;
}