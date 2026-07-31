package com.movem.backend.entity.Group;

import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Group.InviteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_invites")
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