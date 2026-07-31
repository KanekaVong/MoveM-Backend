package com.movem.backend.service.implement;

import com.movem.backend.dto.response.ActivityFeedResponse;
import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.FeedsAndLogs.ActivityFeed;
import com.movem.backend.entity.User;
import com.movem.backend.exception.ResourceNotFoundException;
import com.movem.backend.mapper.SharedMapper.ActivityFeedMapper;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.repository.SharedRepository.ActivityFeedRepository;
import com.movem.backend.repository.SharedRepository.ActivityRepository;
import com.movem.backend.service.AuthServices.CurrentUserService;
import com.movem.backend.service.SharedServices.ActivityFeedService;
import com.movem.backend.service.SharedServices.ActivityPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
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
            Long referenceId
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
    public List<ActivityFeedResponse> getActivityFeed(
            String activityId
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

        return activityFeedRepository
                .findByActivityOrderByCreatedAtDesc(activity)
                .stream()
                .map(activityFeedMapper::toResponse)
                .toList();

    }

}