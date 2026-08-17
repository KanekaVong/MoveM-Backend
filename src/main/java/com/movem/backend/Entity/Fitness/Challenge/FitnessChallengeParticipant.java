package com.movem.backend.Entity.Fitness.Challenge;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Fitness.FitnessChallengeParticipantStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "fitness_challenge_participant",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_challenge_participant",
                        columnNames = {
                                "challenge_id",
                                "user_id"
                        }
                )
        }
)
@Getter
@Setter
public class FitnessChallengeParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "challenge_id",
            nullable = false
    )
    private GroupFitnessChallenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private FitnessChallengeParticipantStatus status;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;


}
