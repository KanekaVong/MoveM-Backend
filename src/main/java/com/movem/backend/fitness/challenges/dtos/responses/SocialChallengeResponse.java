package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessChallengeStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialChallengeResponse {
     Integer challengeId;
     String name;
     String description;
     String workoutType;
     BigDecimal targetValue;
     String targetUnit;
     FitnessChallengeStatus status;
     LocalDateTime startAt;
     LocalDateTime endAt;

     Integer creatorId;
     String creatorUsername;

     long participantCount;
     long completedParticipants;

     BigDecimal myProgress;
     boolean myCompleted;
}