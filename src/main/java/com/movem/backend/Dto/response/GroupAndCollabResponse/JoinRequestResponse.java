package com.movem.backend.Dto.response.GroupAndCollabResponse;

import com.movem.backend.model.enums.Collaboration.JoinRequestStatus;
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