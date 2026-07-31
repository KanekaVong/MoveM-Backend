package com.movem.backend.controller.SharedController;

import com.movem.backend.dto.response.ActivityFeedResponse;
import com.movem.backend.service.SharedServices.ActivityFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activity-feed")
@RequiredArgsConstructor
public class ActivityFeedController {

    private final ActivityFeedService activityFeedService;

    @GetMapping("/{activityId}")
    public List<ActivityFeedResponse> getActivityFeed(
            @PathVariable String activityId
    ) {

        return activityFeedService.getActivityFeed(
                activityId
        );

    }

}