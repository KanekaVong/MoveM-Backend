package com.movem.backend.Service.Implement.ActivityFeed;

import com.movem.backend.Dto.response.ActivityFeedResponse;
import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.FeedsAndLogs.ActivityFeed;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Exception.ResourceNotFoundException;
import com.movem.backend.Mapper.SharedMapper.ActivityFeedMapper;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.Repository.SharedRepository.ActivityFeedRepository;
import com.movem.backend.Repository.SharedRepository.ActivityRepository;
import com.movem.backend.Service.AuthServices.CurrentUserService;
import com.movem.backend.Service.SharedServices.ActivityFeedService;
import com.movem.backend.Service.SharedServices.ActivityPermissionService;
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