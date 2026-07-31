package com.movem.backend.entity.Tasks;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_checklists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_activity_id")
    private Task task;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}