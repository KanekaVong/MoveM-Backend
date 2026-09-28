package com.movem.backend.fitness.achievement.entities;

import com.movem.backend.authentication.entities.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_achievements")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserAchievement {

    @EmbeddedId
    UserAchievementId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("achievementId")
    @JoinColumn(name = "achievement_id", nullable = false)
    Achievement achievement;

    @Column(name = "earned_at")
    LocalDateTime earnedAt;

    @Column(nullable = false)
    boolean notified = false;
}