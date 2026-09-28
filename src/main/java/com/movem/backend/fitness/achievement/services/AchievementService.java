package com.movem.backend.fitness.achievement.services;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.Event.FeatureEvent;
import com.movem.backend.fitness.achievement.dtos.responses.AchievementResponse;
import com.movem.backend.fitness.achievement.dtos.responses.UserAchievementResponse;
import com.movem.backend.fitness.achievement.entities.UserAchievement;

import java.util.List;

public interface AchievementService {
    List<UserAchievement> evaluate(User user, FeatureEvent event);
    long getMyAchievementCount();
    List<UserAchievementResponse> getMyAchievements();
    List<AchievementResponse> getAllAchievements();
    List<UserAchievementResponse> getCurrentAchievement();
    List<UserAchievementResponse> toResponse(List<UserAchievement> achievements);
}