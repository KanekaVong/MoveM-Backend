package com.movem.backend.shared.group.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.InviteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "group_invites",
        indexes = {

                @Index(
                        name = "idx_groupinvite_group",
                        columnList = "group_id"
                ),

                @Index(
                        name = "idx_groupinvite_invitee",
                        columnList = "invitee_id"
                ),

                @Index(
                        name = "idx_groupinvite_inviter",
                        columnList = "inviter_id"
                ),

                @Index(
                        name = "idx_groupinvite_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
public class GroupInvite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "group_id",
            nullable = false
    )
    private ActivityGroup activityGroup;

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