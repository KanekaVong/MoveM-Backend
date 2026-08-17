package com.movem.backend.Entity.Fitness.Club;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Collaboration.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "fitness_club_join_requests",
        indexes = {

                @Index(
                        name = "idx_fitness_club_request_club",
                        columnList = "club_id"
                ),

                @Index(
                        name = "idx_fitness_club_request_user",
                        columnList = "requester_id"
                ),

                @Index(
                        name = "idx_fitness_club_request_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
public class FitnessClubJoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "club_id",
            nullable = false
    )
    private FitnessClub fitnessClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "requester_id",
            nullable = false
    )
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private JoinRequestStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
}