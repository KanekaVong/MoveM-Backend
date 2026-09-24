package com.movem.backend.fitness.club.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.InviteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "fitness_club_invites",
        indexes = {
                @Index(
                        name = "idx_fitnessclubinvite_club",
                        columnList = "club_id"
                ),
                @Index(
                        name = "idx_fitnessclubinvite_invitee",
                        columnList = "invitee_id"
                ),
                @Index(
                        name = "idx_fitnessclubinvite_inviter",
                        columnList = "inviter_id"
                ),
                @Index(
                        name = "idx_fitnessclubinvite_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
public class FitnessClubInvite {

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
            name = "inviter_id",
            nullable = false
    )
    private User inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "invitee_id",
            nullable = false
    )
    private User invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InviteStatus status;

    private LocalDateTime invitedAt;

    private LocalDateTime respondedAt;
}
