package com.movem.backend.fitness.achievement.repositories;

import com.movem.backend.fitness.achievement.entities.Achievement;
import com.movem.backend.fitness.achievement.entities.UserAchievement;
import com.movem.backend.fitness.achievement.entities.UserAchievementId;
import com.movem.backend.authentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAchievementRepository
        extends JpaRepository<UserAchievement, UserAchievementId> {

    List<UserAchievement> findByUserOrderByEarnedAtDesc(
            User user
    );

    boolean existsByUserAndAchievement(
            User user,
            Achievement achievement
    );

    long countByUser(User user);
}