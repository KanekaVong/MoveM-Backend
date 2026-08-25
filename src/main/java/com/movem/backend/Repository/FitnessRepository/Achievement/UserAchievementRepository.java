package com.movem.backend.Repository.FitnessRepository.Achievement;

import com.movem.backend.Entity.Achievement.Achievement;
import com.movem.backend.Entity.Achievement.UserAchievement;
import com.movem.backend.Entity.Achievement.UserAchievementId;
import com.movem.backend.Entity.Auth.User;
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