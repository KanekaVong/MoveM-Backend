package com.movem.backend.Entity.Fitness.ProfileAndGoal;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Fitness.GoalType;
import com.movem.backend.model.enums.Fitness.WorkoutLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_goal")
@Getter
@Setter
public class FitnessGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "goal_type", nullable = false)
    private GoalType goalType;

    @Column(name = "target_weight", precision = 5, scale = 2)
    private BigDecimal targetWeight;

    @Column(name = "target_timeline")
    private LocalDate targetTimeline;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_level")
    private WorkoutLevel workoutLevel;

    @Column(name = "estimated_weight_change", precision = 5, scale = 2)
    private BigDecimal estimatedWeightChange;

    @Column(name = "estimaated_daily_deficit", precision = 8, scale = 2)
    private BigDecimal estimatedDailyDeficit;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
