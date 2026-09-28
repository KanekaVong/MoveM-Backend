package com.movem.backend.fitness.profileandgoal.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Fitness.GoalType;
import com.movem.backend.commons.enums.Fitness.WorkoutLevel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_goal")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false)
    GoalType goalType;

    @Column(name = "target_weight", precision = 5, scale = 2)
    BigDecimal targetWeight;

    @Column(name = "target_timeline")
    LocalDate targetTimeline;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_level")
    WorkoutLevel workoutLevel;

    @Column(name = "estimated_weight_change", precision = 5, scale = 2)
    BigDecimal estimatedWeightChange;

    @Column(name = "estimaated_daily_deficit", precision = 8, scale = 2)
    BigDecimal estimatedDailyDeficit;

    @Column(name = "status", length = 30) String status;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

}
