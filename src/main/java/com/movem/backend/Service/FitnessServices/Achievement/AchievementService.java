package com.movem.backend.Service.FitnessServices.Achievement;

import com.movem.backend.Dto.response.FitnessResponse.Achievement.AchievementResponse;
import com.movem.backend.Dto.response.FitnessResponse.Achievement.UserAchievementResponse;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.Event.FeatureEvent;

import java.util.List;

public interface AchievementService {

    void evaluate(User user, FeatureEvent event);

    long getMyAchievementCount();

    List<UserAchievementResponse> getMyAchievements();

    List<AchievementResponse> getAllAchievements();
}