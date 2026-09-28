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
public class GroupInviteResponse {
     Long inviteId;
     Integer groupId;
     String activityId;
     String activityName;
     Integer inviterId;
     String inviterUsername;
     Integer inviteeId;
     String inviteeUsername;
     InviteStatus status;
     LocalDateTime invitedAt;
     LocalDateTime respondedAt;
}