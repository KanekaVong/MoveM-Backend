package com.movem.backend.Entity.Shared;

import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Notification.NotificationType;
import com.movem.backend.model.enums.Notification.ReferenceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notification",
        indexes = {

                @Index(
                        name = "idx_notification_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_notification_read",
                        columnList = "is_read"
                ),

                @Index(
                        name = "idx_notification_created",
                        columnList = "created_at"
                ),

                @Index(
                        name = "idx_notification_reference",
                        columnList = "reference_type, reference_id"
                )

        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Receiver of notification
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    private ReferenceType referenceType;

    @Column(name = "reference_id", length = 20)
    private String referenceId;

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

}