package com.movem.backend.authentication.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_devices", indexes = {@Index(name = "idx_user_device_user", columnList = "user_id"), @Index(name = "idx_user_device_token", columnList = "device_token")},uniqueConstraints = {@UniqueConstraint(name = "uk_user_device_token", columnNames = "device_token")})
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "device_token", nullable = false, length = 500, unique = true)
    String deviceToken;

    @Column(name = "platform", length = 20)
    String platform;

    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;

    @Column(name = "last_seen_at")
    LocalDateTime lastSeenAt;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}