package com.movem.backend.service.Implement;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Tasks.TaskLabel;
import com.movem.backend.entity.User;
import com.movem.backend.exception.ResourceNotFoundException;
import com.movem.backend.exception.UnauthorizedActionException;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import com.movem.backend.repository.CommentRepository.CommentRepository;
import com.movem.backend.repository.GroupRepository.GroupRepository;
import com.movem.backend.repository.SharedRepository.ActivityFeedRepository;
import com.movem.backend.repository.SharedRepository.ActivityRepository;
import com.movem.backend.repository.SharedRepository.AuditLogRepository;
import com.movem.backend.repository.TaskRepositories.TaskLabelRepository;
import com.movem.backend.service.AuthServices.CurrentUserService;
import com.movem.backend.service.SharedServices.ActivityDeletionService;
import com.movem.backend.service.SharedServices.ActivityService;
import com.movem.backend.service.SharedServices.AuditLogService;
import com.movem.backend.util.*;
import com.movem.backend.util.Base.BaseActivityCreateSource;
import com.movem.backend.util.Base.BaseActivityUpdateSource;
import com.movem.backend.util.TripUtil.TripCreateSource;
import com.movem.backend.util.TripUtil.TripUpdateSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityIdGenerator activityIdGenerator;
    private final TaskLabelRepository taskLabelRepository;
    private final CommentRepository commentRepository;
    private final AuditLogRepository auditLogRepository;
    private final GroupRepository groupRepository;
    private final ActivityFeedRepository activityFeedRepository;
    private final CurrentUserService currentUserService;
    private final AuditLogService auditLogService;
    private final ActivityDeletionService activityDeletionService;

    @Override
    public Activity createActivity(
            BaseActivityCreateSource source,
            User user,
            ActivityType activityType
    ) {

        Activity activity = new Activity();

        activity.setId(activityIdGenerator.generate());

        applyCommonFields(activity, source);

        if (source instanceof TripCreateSource tripSource) {
            applyTripFields(activity, tripSource);
        }

        activity.setActivityType(activityType);
        activity.setStatus(ActivityStatus.PENDING);
        activity.setUser(user);

        activity.setCreatedAt(LocalDateTime.now());
        activity.setUpdatedAt(LocalDateTime.now());

        if (source.getParentActivityId() != null &&
                !source.getParentActivityId().isBlank()) {

            Activity parent = activityRepository.findById(source.getParentActivityId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Parent activity not found."));

            activity.setParentActivity(parent);
        }

        return activityRepository.save(activity);
    }

    @Override
    public Activity updateActivity(
            Activity activity,
            BaseActivityUpdateSource source
    ) {

        applyCommonUpdateFields(activity, source);

        if (source instanceof TripUpdateSource tripSource) {
            applyTripUpdateFields(activity, tripSource);
        }

        activity.setUpdatedAt(LocalDateTime.now());

        return activityRepository.save(activity);
    }

    @Override
    public Activity attachLabels(
            Activity activity,
            List<Integer> labelIds
    ) {

        if (labelIds == null || labelIds.isEmpty()) {
            return activity;
        }

        Set<TaskLabel> labels = new HashSet<>(
                taskLabelRepository.findByIdInAndUser(
                        labelIds,
                        activity.getUser()
                )
        );

        if (labels.size() != labelIds.size()) {
            throw new ResourceNotFoundException(
                    "One or more task labels were not found or do not belong to the current user."
            );
        }

        activity.getLabels().clear();
        activity.getLabels().addAll(labels);

        return activityRepository.save(activity);
    }

    @Override
    @Transactional
    public void permanentlyDeleteActivity(String activityId) {

        User currentUser = currentUserService.getCurrentUser();

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Activity not found."));

        if (!activity.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException(
                    "You can only permanently delete your own activity."
            );
        }

        auditLogRepository.detachActivity(activity.getId());

        activityDeletionService.permanentlyDelete(activity);

        auditLogService.createDeletedActivityLog(
                activity.getId(),
                activity.getActivityName(),
                currentUser,
                ActivityFeedEvent.ACTIVITY_HARD_DELETED,
                AuditCategory.TASK,
                AuditSeverity.WARNING,
                "activity",
                "Activity permanently deleted.",
                activity.getActivityName(),
                null
        );
    }

    private void applyCommonFields(
            Activity activity,
            BaseActivityCreateSource source
    ) {
        activity.setActivityName(source.getActivityName());
        activity.setDescription(source.getDescription());
        activity.setStartActivity(source.getStartActivity());
        activity.setDeadline(source.getDeadline());
    }

    private void applyTripFields(
            Activity activity,
            TripCreateSource source
    ) {
        activity.setLocationName(source.getLocationName());
        activity.setLocationAddress(source.getLocationAddress());
        activity.setLat(source.getLat());
        activity.setLng(source.getLng());
        activity.setGooglePlaceId(source.getGooglePlaceId());
        activity.setCoordinates(source.getCoordinates());
    }

    private void applyCommonUpdateFields(
            Activity activity,
            BaseActivityUpdateSource source
    ) {
        activity.setActivityName(source.getActivityName());
        activity.setDescription(source.getDescription());
        activity.setDeadline(source.getDeadline());
    }

    private void applyTripUpdateFields(
            Activity activity,
            TripUpdateSource source
    ) {
        activity.setLocationName(source.getLocationName());
        activity.setLocationAddress(source.getLocationAddress());
        activity.setLat(source.getLat());
        activity.setLng(source.getLng());
        activity.setGooglePlaceId(source.getGooglePlaceId());
        activity.setCoordinates(source.getCoordinates());
    }



}