package com.movem.backend.fitness.challenges.dtos.responses;

import com.movem.backend.commons.enums.Fitness.FitnessChallengeParticipantStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessChallengeParticipantResponse {
     Integer id;
     Integer challengeId;
     Integer userId;
     LocalDateTime joinedAt;
     LocalDateTime completedAt;
     FitnessChallengeParticipantStatus status;
}