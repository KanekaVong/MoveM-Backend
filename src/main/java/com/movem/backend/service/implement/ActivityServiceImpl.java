package com.movem.backend.service.implement;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.Tasks.TaskLabel;
import com.movem.backend.entity.User;
import com.movem.backend.exception.ResourceNotFoundException;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import com.movem.backend.repository.SharedRepository.ActivityRepository;
import com.movem.backend.repository.TaskRepositories.TaskLabelRepository;
import com.movem.backend.service.SharedServices.ActivityService;
import com.movem.backend.util.ActivityCreateSource;
import com.movem.backend.util.ActivityIdGenerator;
import com.movem.backend.util.ActivityUpdateSource;
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

    @Override
    public Activity createActivity(
            ActivityCreateSource source,
            User user,
            ActivityType activityType
    ) {

        Activity activity = new Activity();

        activity.setId(activityIdGenerator.generate());

        applyCreateFields(activity, source);

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
            ActivityUpdateSource source
    ) {

        applyUpdateFields(activity, source);

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

    private void applyCreateFields(
            Activity activity,
            ActivityCreateSource source
    ) {
        activity.setActivityName(source.getActivityName());
        activity.setDescription(source.getDescription());
        activity.setStartActivity(source.getStartActivity());
        activity.setDeadline(source.getDeadline());
        activity.setLocationName(source.getLocationName());
        activity.setLocationAddress(source.getLocationAddress());
        activity.setLat(source.getLat());
        activity.setLng(source.getLng());
        activity.setGooglePlaceId(source.getGooglePlaceId());
        activity.setCoordinates(source.getCoordinates());
    }

    private void applyUpdateFields(
            Activity activity,
            ActivityUpdateSource source
    ) {
        activity.setActivityName(source.getActivityName());
        activity.setDescription(source.getDescription());
        activity.setStartActivity(source.getStartActivity());
        activity.setDeadline(source.getDeadline());
        activity.setLocationName(source.getLocationName());
        activity.setLocationAddress(source.getLocationAddress());
        activity.setLat(source.getLat());
        activity.setLng(source.getLng());
        activity.setGooglePlaceId(source.getGooglePlaceId());
        activity.setCoordinates(source.getCoordinates());
        activity.setStatus(source.getStatus());
    }
}