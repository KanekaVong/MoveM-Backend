package com.movem.backend.controller.SharedController;

import com.movem.backend.dto.response.ActivityFeedResponse;
import com.movem.backend.service.SharedServices.ActivityFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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