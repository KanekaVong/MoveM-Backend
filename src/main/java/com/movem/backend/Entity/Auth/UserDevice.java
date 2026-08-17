package com.movem.backend.Entity.Auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_devices",
        indexes = {
                @Index(
                        name = "idx_user_device_user",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_user_device_token",
                        columnList = "device_token"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_device_token",
                        columnNames = "device_token"
                )
        }
)
@Getter
@Setter
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(
            name = "device_token",
            nullable = false,
            length = 500,
            unique = true
    )
    private String deviceToken;

    @Column(
            name = "platform",
            length = 20
    )
    private String platform;

    @Column(
            name = "is_active",
            nullable = false
    )
    private Boolean isActive = true;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}