package com.movem.backend.service.SharedServices;

import com.movem.backend.dto.response.ActivityFeedResponse;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;

import java.util.List;

public interface ActivityFeedService {

    void createFeed(
            Activity activity,
            User user,
            ActivityFeedEvent eventType,
            String message,
            Long referenceId
    );

    List<ActivityFeedResponse> getActivityFeed(
            String activityId
    );

}