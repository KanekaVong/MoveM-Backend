package com.movem.backend.shared.group.dtos.responses;

import com.movem.backend.commons.enums.shared.JoinRequestStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinRequestResponse {
     Long requestId;
     Integer groupId;
     String activityId;
     String activityName;
     Integer requesterId;
     String requesterUsername;
     JoinRequestStatus status;
     LocalDateTime requestedAt;
     LocalDateTime respondedAt;
}