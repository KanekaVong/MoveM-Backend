package com.movem.backend.shared.historyandlogs.activityfeed.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ActivityFeedResponse {

    private Long id;

    private String activityId;

    private Integer userId;

    private String username;

    private String firstname;

    private String lastname;

    private String profilePic;

    private ActivityFeedEvent eventType;

    private String message;

    private String referenceId;

    private LocalDateTime createdAt;

}