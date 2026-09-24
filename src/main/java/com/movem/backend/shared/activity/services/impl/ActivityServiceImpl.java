package com.movem.backend.shared.activity.services.impl;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.task.entities.TaskLabel;
import com.movem.backend.commons.Exception.ResourceNotFoundException;
import com.movem.backend.commons.Exception.UnauthorizedActionException;
import com.movem.backend.task.mappers.TaskMapper;
import com.movem.backend.shared.activity.repositories.ActivityRepository;
import com.movem.backend.shared.historyandlogs.auditlog.repositories.AuditLogRepository;
import com.movem.backend.task.repositories.TaskLabelRepository;
import com.movem.backend.task.repositories.TaskRepository;
import com.movem.backend.authentication.services.CurrentUserService;
import com.movem.backend.shared.historyandlogs.featureevents.services.FeatureEventTrackingService;
import com.movem.backend.shared.activity.services.ActivityDeletionService;
import com.movem.backend.shared.activity.services.ActivityPermissionService;
import com.movem.backend.shared.activity.services.ActivityService;
import com.movem.backend.commons.Util.ActivityIdGenerator;
import com.movem.backend.commons.Util.BaseUtil.BaseActivityCreateSource;
import com.movem.backend.commons.Util.BaseUtil.BaseActivityUpdateSource;
import com.movem.backend.commons.Util.TripUtil.TripCreateSource;
import com.movem.backend.commons.Util.TripUtil.TripUpdateSource;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.shared.ActivityType;
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
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AuditLogRepository auditLogRepository;
    private final CurrentUserService currentUserService;
    private final ActivityPermissionService activityPermissionService;
    private final FeatureEventTrackingService featureEventTrackingService;
    private final ActivityDeletionService activityDeletionService;

    @Override
    @Transactional
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

        if (source.getParentActivityId() != null && !source.getParentActivityId().isBlank()) {
            Activity parent = activityRepository.findById(source.getParentActivityId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent activity not found: " + source.getParentActivityId()));

            activity.setParentActivity(parent);
        }

        return activityRepository.saveAndFlush(activity);
    }

    @Override
    public Activity updateActivity(Activity activity, BaseActivityUpdateSource source) {
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
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));

        if (!activity.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException(
                    "You can only permanently delete your own activity."
            );
        }

        auditLogRepository.detachActivity(activity.getId());
        activityDeletionService.permanentlyDelete(activity);

        featureEventTrackingService.handleDeletedActivity(
                activity.getId(),
                activity.getActivityName(),
                currentUser
        );
    }

    // Helper Methods

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
        activity.setStartActivity(source.getStartActivity());
        activity.setDeadline(source.getDeadline());
    }
}