package com.movem.backend.Controller.SharedController;

import com.movem.backend.Dto.response.ActivityFeedResponse;
import com.movem.backend.Service.SharedServices.ActivityFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity-feed")
@RequiredArgsConstructor
public class ActivityFeedController {

    private final ActivityFeedService activityFeedService;

    @GetMapping("/{activityId}")
    public Page<ActivityFeedResponse> getActivityFeed(
            @PathVariable String activityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {

        return activityFeedService.getActivityFeed(
                activityId,
                page,
                size
        );

    }

}