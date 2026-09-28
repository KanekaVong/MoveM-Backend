package com.movem.backend.social.like.entity;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.fitness.workout.entities.FitnessWorkoutSession;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_workout_kudos",
        uniqueConstraints = @UniqueConstraint(name = "uk_workout_kudos_user", columnNames = {"workout_session_id", "user_id"}))
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Kudos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false)
    FitnessWorkoutSession workoutSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}