package com.movem.backend.shared.group.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.InviteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_invites", indexes = {@Index(name = "idx_groupinvite_group", columnList = "group_id"),
                @Index(name = "idx_groupinvite_invitee", columnList = "invitee_id"),
                @Index(name = "idx_groupinvite_inviter", columnList = "inviter_id"),
                @Index(name = "idx_groupinvite_status", columnList = "status")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupInvite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    ActivityGroup activityGroup;

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