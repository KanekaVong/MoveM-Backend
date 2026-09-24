package com.movem.backend.shared.historyandlogs.activityfeed.services;

import com.movem.backend.shared.historyandlogs.activityfeed.dtos.responses.ActivityFeedResponse;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import org.springframework.data.domain.Page;

public interface ActivityFeedService {

    void createFeed(
            Activity activity,
            User user,
            ActivityFeedEvent eventType,
            String message,
            String referenceId
    );

    Page<ActivityFeedResponse> getActivityFeed(
            String activityId,
            int page,
            int size
    );

}