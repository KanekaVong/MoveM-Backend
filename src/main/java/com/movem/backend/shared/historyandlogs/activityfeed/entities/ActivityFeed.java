package com.movem.backend.shared.historyandlogs.activityfeed.entities;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_feed", indexes = {@Index(name = "idx_activityfeed_activity", columnList = "activity_id"),
                @Index(name = "idx_activityfeed_user", columnList = "user_id"),
                @Index(name = "idx_activityfeed_created", columnList = "createdAt"),
                @Index(name = "idx_activityfeed_event", columnList = "eventType")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ActivityFeedEvent eventType;

    @Column(nullable = false, length = 500)
    String message;

    String referenceId;

    @Column(nullable = false)
    LocalDateTime createdAt;

}