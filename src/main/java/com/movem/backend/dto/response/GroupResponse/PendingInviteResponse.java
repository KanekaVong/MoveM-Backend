package com.movem.backend.dto.response.GroupResponse;

import com.movem.backend.model.enums.Group.InviteStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PendingInviteResponse {

    private Long inviteId;

    private Integer groupId;

    private String activityId;

    private String activityName;

    private Integer inviteeId;

    private String inviteeUsername;

    private String inviteeEmail;

    private InviteStatus status;

    private LocalDateTime invitedAt;

}