package com.movem.backend.Entity.Fitness.WorkoutSession;


import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Fitness.Challenge.FitnessChallengeParticipant;
import com.movem.backend.Entity.Fitness.Challenge.SoloChallenge;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.model.enums.Fitness.WorkoutType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fitness_workout_session")
@Getter
@Setter
public class FitnessWorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "activity_id",
            nullable = false,
            unique = true
    )
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solo_challenge_id")
    private SoloChallenge soloChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_challenge_participant_id")
    private FitnessChallengeParticipant
            groupChallengeParticipant;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "workout_type",
            nullable = false
    )
    private WorkoutType workoutType;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private FitnessWorkoutStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "paused_at")
    private LocalDateTime pausedAt;

    @Column(
            name = "total_paused_seconds",
            nullable = false
    )
    private Integer totalPausedSeconds = 0;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(
            name = "duration_seconds",
            nullable = false
    )
    private Integer durationSeconds = 0;

    @Column(
            nullable = false
    )
    private Integer steps = 0;

    @Column(
            precision = 10,
            scale = 2,
            nullable = false
    )
    private BigDecimal distance = BigDecimal.ZERO;

    @Column(
            name = "calories_burned",
            precision = 10,
            scale = 2,
            nullable = false
    )
    private BigDecimal caloriesBurned = BigDecimal.ZERO;

    @Column(
            name = "average_pace",
            precision = 10,
            scale = 2
    )
    private BigDecimal averagePace;

    @Column(
            name = "gps_route",
            columnDefinition = "LONGTEXT"
    )
    private String gpsRoute;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(
            name = "average_speed",
            precision = 10,
            scale = 2
    )
    private BigDecimal averageSpeed;

    @OneToMany(
            mappedBy = "workoutSession",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("pointSequence ASC")
    private List<FitnessWorkoutRoutePoint> routePoints =
            new ArrayList<>();
}
