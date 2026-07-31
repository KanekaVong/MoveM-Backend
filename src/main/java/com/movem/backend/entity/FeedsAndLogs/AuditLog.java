package com.movem.backend.entity.FeedsAndLogs;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.entity.User;
import com.movem.backend.model.enums.Activity.ActivityFeedEvent;
import com.movem.backend.model.enums.Audit.AuditCategory;
import com.movem.backend.model.enums.Audit.AuditSeverity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "audit_logs",
        indexes = {

                @Index(
                        name = "idx_audit_activity",
                        columnList = "activity_id"
                ),

                @Index(
                        name = "idx_audit_user",
                        columnList = "user_id"
                ),

                @Index(
                        name = "idx_audit_category",
                        columnList = "category"
                ),

                @Index(
                        name = "idx_audit_severity",
                        columnList = "severity"
                ),

                @Index(
                        name = "idx_audit_created",
                        columnList = "createdAt"
                ),

                @Index(
                        name = "idx_audit_event",
                        columnList = "eventType"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = true)
    private Activity activity;

    @Column(name = "activity_code", length = 10)
    private String activityId;

    @Column(name = "activity_name")
    private String activityName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private AuditCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    private AuditSeverity severity;

    @Column(name = "field_changed")
    private String fieldChanged;

    @Enumerated(EnumType.STRING)
    private ActivityFeedEvent eventType;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String oldValue;

    @Column(columnDefinition = "TEXT")
    private String newValue;

    private LocalDateTime createdAt;

}