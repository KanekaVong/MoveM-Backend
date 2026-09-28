package com.movem.backend.fitness.workout.entities;


import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.fitness.challenges.entities.FitnessChallengeParticipant;
import com.movem.backend.fitness.challenges.entities.SoloChallenge;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Fitness.FitnessWorkoutStatus;
import com.movem.backend.commons.enums.Fitness.TrackingMode;
import com.movem.backend.commons.enums.Fitness.WorkoutType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fitness_workout_session")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessWorkoutSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false, unique = true)
    Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solo_challenge_id")
    SoloChallenge soloChallenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_challenge_participant_id")
    FitnessChallengeParticipant groupChallengeParticipant;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    WorkoutType workoutType;

    @Enumerated(EnumType.STRING)
    @Column(name = "tracking_mode", nullable = false, length = 20)
    TrackingMode trackingMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    FitnessWorkoutStatus status;

    @Column(name = "started_at")
    LocalDateTime startedAt;

    @Column(name = "paused_at")
    LocalDateTime pausedAt;

    @Column(name = "is_shared", nullable = false)
    Boolean isShared = false;

    @Column(name = "share_description", columnDefinition = "TEXT")
    String shareDescription;

    @Column(name = "total_paused_seconds", nullable = false)
    Integer totalPausedSeconds = 0;

    @Column(name = "finished_at")
    LocalDateTime finishedAt;

    @Column(name = "duration_seconds", nullable = false)
    Integer durationSeconds = 0;

    @Column(nullable = false)
    Integer steps = 0;

    @Column(precision = 10, scale = 2, nullable = false)
    BigDecimal distance = BigDecimal.ZERO;

    @Column(name = "calories_burned", precision = 10, scale = 2, nullable = false)
    BigDecimal caloriesBurned = BigDecimal.ZERO;

    @Column(name = "average_pace", precision = 10, scale = 2)
    BigDecimal averagePace;

    @Column(name = "gps_route", columnDefinition = "LONGTEXT")
    String gpsRoute;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @Column(name = "average_speed", precision = 10, scale = 2)
    BigDecimal averageSpeed;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Attachment> attachments = new ArrayList<>();
}
