package com.movem.backend.Entity.Friend;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Friend.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "friend_request",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_friend_request",
                        columnNames = {"sender_id", "receiver_id"}
                )
        },
        indexes = {

                @Index(
                        name = "idx_friend_request_sender",
                        columnList = "sender_id"
                ),

                @Index(
                        name = "idx_friend_request_receiver",
                        columnList = "receiver_id"
                ),

                @Index(
                        name = "idx_friend_request_status",
                        columnList = "status"
                ),

                @Index(
                        name = "idx_friend_request_created",
                        columnList = "createdAt"
                )
        }
)
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendRequestStatus status = FriendRequestStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime respondedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}