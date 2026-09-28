package com.movem.backend.shared.group.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.JoinRequestStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "join_requests", indexes = {@Index(name = "idx_joinrequest_group", columnList = "group_id"),
                @Index(name = "idx_joinrequest_requester", columnList = "requester_id"),
                @Index(name = "idx_joinrequest_status", columnList = "status")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JoinRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    ActivityGroup activityGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    JoinRequestStatus status;

    @Column(nullable = false)
    LocalDateTime requestedAt;
    LocalDateTime respondedAt;

}