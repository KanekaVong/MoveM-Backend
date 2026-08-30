package com.movem.backend.Entity.FeedsAndLogs;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "activity_feed",
        indexes = {

                @Index(
                        name = "idx_activityfeed_activity",
                        columnList = "activity_id"
                ),

                @Index(
                        name = "idx_activityfeed_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_activityfeed_created",
                        columnList = "createdAt"
                ),

                @Index(
                        name = "idx_activityfeed_event",
                        columnList = "eventType"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityFeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "activity_id",
            nullable = false
    )
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityFeedEvent eventType;

    @Column(nullable = false, length = 500)
    private String message;

    private String referenceId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}