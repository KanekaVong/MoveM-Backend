package com.movem.backend.shared.activity.repositories;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.shared.ActivityType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, String> {

    @EntityGraph(attributePaths = {"labels"})
    List<Activity> findByUser(User user);
    @EntityGraph(attributePaths = {"labels"})
    List<Activity> findByUserAndActivityType(User user, ActivityType activityType);
    @EntityGraph(attributePaths = {"labels"})
    List<Activity> findByUserAndStatus(User user, ActivityStatus status);
    List<Activity> findByDeletedAtBefore(LocalDateTime cutoff);
    List<Activity> findByStatusAndDeletedAtBefore(ActivityStatus activityStatus, LocalDateTime cutoff);
    long countByUserAndIsCollaborativeTrueAndStatusNot(User currentUser, ActivityStatus activityStatus);
    long countByUserAndIsCollaborativeFalseAndStatusNot(User currentUser, ActivityStatus activityStatus);
    List<Activity> findByDeletedAtIsNotNullAndDeletedAtBefore(LocalDateTime cutoff);
}