package com.movem.backend.fitness.achievement.services;

import com.movem.backend.fitness.achievement.dtos.responses.AchievementResponse;
import com.movem.backend.fitness.achievement.dtos.responses.UserAchievementResponse;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Event.FeatureEvent;

import java.util.List;

public interface AchievementService {

    void evaluate(User user, FeatureEvent event);

    long getMyAchievementCount();

    List<UserAchievementResponse> getMyAchievements();

    List<AchievementResponse> getAllAchievements();
}