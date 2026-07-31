package com.movem.backend.entity.Group;

import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Group.InviteStatus;
import com.movem.backend.model.enums.Group.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "join_requests",
        indexes = {

                @Index(
                        name = "idx_joinrequest_group",
                        columnList = "group_id"
                ),

                @Index(
                        name = "idx_joinrequest_requester",
                        columnList = "requester_id"
                ),

                @Index(
                        name = "idx_joinrequest_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
public class JoinRequest {

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
            name = "requester_id",
            nullable = false
    )
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JoinRequestStatus status;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    private LocalDateTime respondedAt;

}