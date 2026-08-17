package com.movem.backend.Entity.Activity;

import com.movem.backend.Entity.Tasks.Task;
import com.movem.backend.Entity.Tasks.TaskLabel;
import com.movem.backend.Entity.Auth.User;
import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Activity.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Activity", indexes = {
        @Index(name = "idx_activity_user", columnList = "user_id"),

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
public class Activity {

    @Id
    @Column(length = 10)
    private String id;

    @Column(name = "activity_name", nullable = false)
    private String activityName;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type")
    private ActivityType activityType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    @Column(name = "start_activity")
    private LocalDateTime startActivity;

    private LocalDateTime deadline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "location_name")
    private String locationName;

    @Column(name = "location_address")
    private String locationAddress;

    private BigDecimal lat;

    private BigDecimal lng;

    @Column(name = "google_place_id")
    private String googlePlaceId;

    // Ignore geometry for now
    @Column(name = "coordinates")
    private String coordinates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_activity")
    private Activity parentActivity;

    @OneToMany(mappedBy = "parentActivity")
    private Set<Activity> childActivities = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "activity_labels",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    private Set<TaskLabel> labels = new HashSet<>();

    @OneToOne(
            mappedBy = "activity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Task task;

    private Boolean isCollaborative = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

//    @OneToOne(mappedBy = "activity")
//    private Fitness fitness;
//
//    @OneToOne(mappedBy = "activity")
//    private Trip trip;
}