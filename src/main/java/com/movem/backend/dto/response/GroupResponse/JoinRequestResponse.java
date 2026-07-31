package com.movem.backend.dto.response.GroupResponse;

import com.movem.backend.model.enums.Group.InviteStatus;
import com.movem.backend.model.enums.Group.JoinRequestStatus;
import jakarta.persistence.criteria.Join;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class JoinRequestResponse {

    private Long requestId;

    private Integer groupId;

    private String activityId;

    private String activityName;

    private Integer requesterId;

    private String requesterUsername;

    private JoinRequestStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime respondedAt;

}