package com.movem.backend.social.friend.entities;

import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.Friend.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "friend_request",
        uniqueConstraints = {@UniqueConstraint(name = "uk_friend_request", columnNames = {"sender_id", "receiver_id"})},
        indexes = {@Index(name = "idx_friend_request_sender", columnList = "sender_id"),
                @Index(name = "idx_friend_request_receiver", columnList = "receiver_id"),
                @Index(name = "idx_friend_request_status", columnList = "status"),
                @Index(name = "idx_friend_request_created", columnList = "createdAt")})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id")
    User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id")
    User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    FriendRequestStatus status = FriendRequestStatus.PENDING;

    @Column(nullable = false)
    LocalDateTime createdAt;

    LocalDateTime respondedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}