package com.movem.backend.shared.historyandlogs.activityfeed.services.impl;

import com.movem.backend.shared.historyandlogs.activityfeed.dtos.responses.ActivityFeedResponse;
import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.shared.historyandlogs.activityfeed.entities.ActivityFeed;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.shared.historyandlogs.activityfeed.mapper.ActivityFeedMapper;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.shared.historyandlogs.activityfeed.repositories.ActivityFeedRepository;
import com.movem.backend.shared.activity.repositories.ActivityRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.shared.historyandlogs.activityfeed.services.ActivityFeedService;
import com.movem.backend.shared.activity.services.ActivityPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Component
public class ActivityFeedServiceImpl
        implements ActivityFeedService {

    private final ActivityFeedRepository activityFeedRepository;

    private final ActivityRepository activityRepository;

    private final ActivityFeedMapper activityFeedMapper;

    private final CurrentUserService currentUserService;

    private final ActivityPermissionService activityPermissionService;

    @Override
    public void createFeed(
            Activity activity,
            User user,
            ActivityFeedEvent eventType,
            String message,
            String referenceId
    ) {

        ActivityFeed feed =
                new ActivityFeed();

        feed.setActivity(activity);
        feed.setUser(user);
        feed.setEventType(eventType);
        feed.setMessage(message);
        feed.setReferenceId(referenceId);
        feed.setCreatedAt(LocalDateTime.now());

        activityFeedRepository.save(feed);

    }

    @Override
    public Page<ActivityFeedResponse> getActivityFeed(
            String activityId,
            int page,
            int size
    ) {

        User currentUser =
                currentUserService.getCurrentUser();

        Activity activity =
                activityRepository.findById(activityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Activity not found."
                                ));

        activityPermissionService.validateActivityAccess(
                activity,
                currentUser
        );

        Pageable pageable =
                PageRequest.of(page, size);

        return activityFeedRepository
                .findByActivityOrderByCreatedAtDesc(
                        activity,
                        pageable
                )
                .map(activityFeedMapper::toResponse);

    }

}