package com.movem.backend.Entity.Fitness.WorkoutSession;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_workout_analysis")
@Getter
@Setter
public class FitnessWorkoutAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "workout_session_id",
            nullable = false,
            unique = true
    )
    private FitnessWorkoutSession workoutSession;

    @Column(nullable = false, length = 50)
    private String exercise;

    @Column(nullable = false)
    private Integer reps = 0;

    @Column(name = "valid_reps", nullable = false)
    private Integer validReps = 0;

    @Column(name = "invalid_reps", nullable = false)
    private Integer invalidReps = 0;

    @Column(name = "form_score")
    private Integer formScore;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}