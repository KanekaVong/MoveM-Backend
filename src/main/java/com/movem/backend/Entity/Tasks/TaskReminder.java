package com.movem.backend.Entity.Tasks;

import com.movem.backend.model.enums.ReminderType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "task_reminders",
        indexes = {

                @Index(
                        name = "idx_reminder_task",
                        columnList = "task_activity_id"
                ),

                @Index(
                        name = "idx_reminder_time",
                        columnList = "remind_at"
                ),

                @Index(
                        name = "idx_reminder_sent",
                        columnList = "is_sent"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_activity_id")
    private Task task;

    @Column(name = "remind_at")
    private LocalDateTime remindAt;

    @Enumerated(EnumType.STRING)
    private ReminderType type;

    @Column(name = "is_sent")
    private Boolean isSent = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}