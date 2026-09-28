package com.movem.backend.fitness.workout.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_workout_route_point", indexes = {@Index(name = "idx_route_point_session", columnList = "workout_session_id"),
                @Index(name = "idx_route_point_sequence", columnList = "workout_session_id, point_sequence")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class FitnessWorkoutRoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workout_session_id", nullable = false)
    FitnessWorkoutSession workoutSession;

    @Column(name = "point_sequence", nullable = false)
    Integer pointSequence;

    @Column(nullable = false, precision = 10, scale = 7)
    BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 7)
    BigDecimal longitude;

    @Column(precision = 8, scale = 2)
    BigDecimal accuracy;

    @Column(precision = 8, scale = 2)
    BigDecimal altitude;

    @Column(name = "recorded_at", nullable = false)
    LocalDateTime recordedAt;
}