package com.movem.backend.Dto.response.GroupAndCollabResponse;

import com.movem.backend.model.enums.Collaboration.InviteStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GroupInviteResponse {

    private Long inviteId;

    private Integer groupId;

    private String activityId;

    private String activityName;

    private Integer inviterId;

    private String inviterUsername;

    private Integer inviteeId;

    private String inviteeUsername;

    private InviteStatus status;

    private LocalDateTime invitedAt;

    private LocalDateTime respondedAt;

}