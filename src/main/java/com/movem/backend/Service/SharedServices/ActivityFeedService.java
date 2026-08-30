package com.movem.backend.Service.SharedServices;

import com.movem.backend.Dto.response.ActivityFeedResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
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