package com.movem.backend.repository.SharedRepository;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, String> {

    List<Activity> findByUser(User user);

    List<Activity> findByUserAndActivityType(User user, ActivityType activityType);

    List<Activity> findByUserAndStatus(User user, ActivityStatus status);
}