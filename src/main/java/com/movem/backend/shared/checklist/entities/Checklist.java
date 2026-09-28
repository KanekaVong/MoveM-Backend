package com.movem.backend.shared.checklist.entities;

import com.movem.backend.task.entities.Task;
import com.movem.backend.trip.entities.Trip;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklists", indexes = {
        @Index(name = "idx_checklist_task", columnList = "task_activity_id"),
        @Index(name = "idx_checklist_trip", columnList = "trip_activity_id"),
        @Index(name = "idx_checklist_completed", columnList = "is_completed")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_activity_id")
    Task task;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id")
    Trip trip;
    @Column(name = "item_name", nullable = false)
    String itemName;
    @Column(name = "is_completed")
    Boolean isCompleted = false;
    @Column(name = "created_at")
    LocalDateTime createdAt;
}