package com.movem.backend.shared.historyandlogs.auditlog.entities;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.commons.enums.shared.ActivityFeedEvent;
import com.movem.backend.commons.enums.HistoryandLogs.AuditCategory;
import com.movem.backend.commons.enums.HistoryandLogs.AuditSeverity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs", indexes = {@Index(name = "idx_audit_activity", columnList = "activity_id"),
                @Index(name = "idx_audit_user", columnList = "user_id"),
                @Index(name = "idx_audit_category", columnList = "category"),
                @Index(name = "idx_audit_severity", columnList = "severity"),
                @Index(name = "idx_audit_created", columnList = "createdAt"),
                @Index(name = "idx_audit_event", columnList = "eventType")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = true)
    Activity activity;

    @Column(name = "activity_code", length = 10)
    String activityId;

    @Column(name = "activity_name")
    String activityName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    AuditCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity")
    AuditSeverity severity;

    @Column(name = "field_changed")
    String fieldChanged;

    @Enumerated(EnumType.STRING)
    ActivityFeedEvent eventType;

    String description;

    @Column(columnDefinition = "TEXT")
    String oldValue;

    @Column(columnDefinition = "TEXT")
    String newValue;

    LocalDateTime createdAt;
}