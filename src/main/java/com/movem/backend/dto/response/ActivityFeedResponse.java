package com.movem.backend.dto.response;

import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
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

    private Long referenceId;

    private LocalDateTime createdAt;

}