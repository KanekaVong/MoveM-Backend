package com.movem.backend.shared.historyandlogs.featureevents.services;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Event.FeatureEvent;
import com.movem.backend.fitness.achievement.entities.UserAchievement;

import java.util.List;

public interface FeatureEventTrackingService {
    List<UserAchievement> handle(FeatureEvent event);
    void handleDeletedActivity(String activityId, String activityName, User actor);
}