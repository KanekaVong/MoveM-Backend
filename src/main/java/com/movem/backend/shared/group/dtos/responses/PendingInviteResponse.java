package com.movem.backend.shared.group.dtos.responses;

import com.movem.backend.commons.enums.shared.InviteStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PendingInviteResponse {
     Long inviteId;
     Integer groupId;
     String activityId;
     String activityName;
     Integer inviteeId;
     String inviteeUsername;
     String inviteeEmail;
     InviteStatus status;
     LocalDateTime invitedAt;
}