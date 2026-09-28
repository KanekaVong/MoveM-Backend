package com.movem.backend.shared.historyandlogs.activityfeed.dtos.responses;

import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityFeedResponse {
     Long id;
     String activityId;
     Integer userId;
     String username;
     String firstname;
     String lastname;
     String profilePic;
     ActivityFeedEvent eventType;
     String message;
     String referenceId;
     LocalDateTime createdAt;

}