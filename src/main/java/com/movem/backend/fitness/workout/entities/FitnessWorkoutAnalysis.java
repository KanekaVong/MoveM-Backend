package com.movem.backend.fitness.workout.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_workout_analysis")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessWorkoutAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false, unique = true)
    FitnessWorkoutSession workoutSession;

    @Column(nullable = false, length = 50)
    String exercise;

    @Column(nullable = false)
    Integer reps = 0;

    @Column(name = "valid_reps", nullable = false)
    Integer validReps = 0;

    @Column(name = "invalid_reps", nullable = false)
    Integer invalidReps = 0;

    @Column(name = "form_score")
    Integer formScore;

    @Column(columnDefinition = "TEXT")
    String feedback;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}