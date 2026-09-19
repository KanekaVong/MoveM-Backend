package com.movem.backend.Entity.Tasks;

import com.movem.backend.Entity.Trip.Trip;
import jakarta.persistence.*;
import lombok.*;

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
public class Checklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_activity_id")
    private Task task;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id")
    private Trip trip;
    @Column(name = "item_name", nullable = false)
    private String itemName;
    @Column(name = "is_completed")
    private Boolean isCompleted = false;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}