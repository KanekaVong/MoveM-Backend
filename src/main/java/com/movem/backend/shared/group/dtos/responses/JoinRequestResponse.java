package com.movem.backend.shared.group.dtos.responses;

import com.movem.backend.commons.enums.shared.JoinRequestStatus;
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