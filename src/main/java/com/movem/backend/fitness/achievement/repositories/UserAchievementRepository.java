package com.movem.backend.fitness.achievement.repositories;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.achievement.entities.Achievement;
import com.movem.backend.fitness.achievement.entities.UserAchievement;
import com.movem.backend.fitness.achievement.entities.UserAchievementId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, UserAchievementId> {
    List<UserAchievement> findByUserOrderByEarnedAtDesc(User user);
    long countByUser(User user);
    Optional<UserAchievement> findByUserAndAchievement(User user, Achievement achievement);
    boolean existsByUserAndAchievement(User user, Achievement achievement);
    List<UserAchievement> findByUserAndNotifiedFalseOrderByEarnedAtDesc(User user);
}