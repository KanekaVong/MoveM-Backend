package com.movem.backend.service.SharedServices;

import com.movem.backend.dto.response.ActivityFeedResponse;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ActivityFeedService {

    void createFeed(
            Activity activity,
            User user,
            ActivityFeedEvent eventType,
            String message,
            Long referenceId
    );

    Page<ActivityFeedResponse> getActivityFeed(
            String activityId,
            int page,
            int size
    );

}