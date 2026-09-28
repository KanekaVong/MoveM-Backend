package com.movem.backend.fitness.club.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.InviteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_club_invites", indexes = {
        @Index(name = "idx_fitnessclubinvite_club", columnList = "club_id"),
                @Index(name = "idx_fitnessclubinvite_invitee", columnList = "invitee_id"),
                @Index(name = "idx_fitnessclubinvite_inviter", columnList = "inviter_id"),
                @Index(name = "idx_fitnessclubinvite_status", columnList = "status")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FitnessClubInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    FitnessClub fitnessClub;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    User inviter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    User invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    InviteStatus status;
    LocalDateTime invitedAt;
    LocalDateTime respondedAt;
}
