package com.movem.backend.Entity.Fitness.WorkoutSession;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "fitness_workout_route_point",
        indexes = {
                @Index(
                        name = "idx_route_point_session",
                        columnList = "workout_session_id"
                ),
                @Index(
                        name = "idx_route_point_sequence",
                        columnList = "workout_session_id, point_sequence"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FitnessWorkoutRoutePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "workout_session_id",
            nullable = false
    )
    private FitnessWorkoutSession workoutSession;

    @Column(
            name = "point_sequence",
            nullable = false
    )
    private Integer pointSequence;

    @Column(
            nullable = false,
            precision = 10,
            scale = 7
    )
    private BigDecimal latitude;

    @Column(
            nullable = false,
            precision = 10,
            scale = 7
    )
    private BigDecimal longitude;

    @Column(
            precision = 8,
            scale = 2
    )
    private BigDecimal accuracy;

    @Column(
            precision = 8,
            scale = 2
    )
    private BigDecimal altitude;

    @Column(
            name = "recorded_at",
            nullable = false
    )
    private LocalDateTime recordedAt;
}