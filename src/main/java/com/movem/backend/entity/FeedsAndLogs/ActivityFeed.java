package com.movem.backend.entity.FeedsAndLogs;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_feed")
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

    private Long referenceId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}