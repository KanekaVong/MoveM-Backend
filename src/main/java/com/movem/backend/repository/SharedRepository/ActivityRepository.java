package com.movem.backend.repository.SharedRepository;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, String> {

    @EntityGraph(attributePaths = {
            "labels"
    })

    List<Activity> findByUser(User user);

    @EntityGraph(attributePaths = {
            "labels"
    })
    List<Activity> findByUserAndActivityType(
            User user,
            ActivityType activityType
    );

    @EntityGraph(attributePaths = {
            "labels"
    })
    List<Activity> findByUserAndStatus(
            User user,
            ActivityStatus status
    );

    List<Activity> findByDeletedAtBefore(LocalDateTime cutoff);

    List<Activity> findByStatusAndDeletedAtBefore(ActivityStatus activityStatus, LocalDateTime cutoff);

    long countByUserAndIsCollaborativeTrueAndStatusNot(User currentUser, ActivityStatus activityStatus);

    long countByUserAndIsCollaborativeFalseAndStatusNot(User currentUser, ActivityStatus activityStatus);

    List<Activity> findByDeletedAtIsNotNullAndDeletedAtBefore(LocalDateTime cutoff);
}