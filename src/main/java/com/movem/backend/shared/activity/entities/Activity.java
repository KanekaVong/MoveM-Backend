package com.movem.backend.shared.activity.entities;

import com.movem.backend.task.entities.Task;
import com.movem.backend.task.entities.TaskLabel;
import com.movem.backend.authentication.entities.User;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.shared.ActivityType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Activity", indexes = {@Index(name = "idx_activity_user", columnList = "user_id"),
        @Index(name = "idx_activity_status", columnList = "status"),
        @Index(name = "idx_activity_type", columnList = "activity_type"),
        @Index(name = "idx_activity_deadline", columnList = "deadline"),
        @Index(name = "idx_activity_parent", columnList = "parent_activity"),
        @Index(name = "idx_activity_deleted", columnList = "deleted_at"),
        @Index(name = "idx_activity_start", columnList = "start_activity") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Activity {
    @Id
    @Column(length = 10)
    String id;

    @Column(name = "activity_name", nullable = false)
    String activityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type")
    ActivityType activityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    ActivityStatus status;

    @Column(name = "start_activity")
    LocalDateTime startActivity;
    LocalDateTime deadline;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "location_name")
    String locationName;

    @Column(name = "location_address")
    String locationAddress;

    BigDecimal lat;
    BigDecimal lng;

    @Column(name = "google_place_id")
    String googlePlaceId;

    @Column(name = "coordinates")
    String coordinates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_activity")
    Activity parentActivity;

    @OneToMany(mappedBy = "parentActivity")
    Set<Activity> childActivities = new HashSet<>();

    @Column(name = "created_at")
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(name = "activity_labels", joinColumns = @JoinColumn(name = "activity_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    Set<TaskLabel> labels = new HashSet<>();

    @OneToOne(mappedBy = "activity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Task task;

    Boolean isCollaborative = false;

    @Column(name = "deleted_at")
    LocalDateTime deletedAt;

    @OneToOne(mappedBy = "activity", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Trip trip;
}