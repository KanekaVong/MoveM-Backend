package com.movem.backend.shared.reminder.entities;

import com.movem.backend.task.entities.Task;
import com.movem.backend.trip.entities.Trip;
import com.movem.backend.commons.enums.shared.ReminderType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "reminders", indexes = {
        @Index(name = "idx_reminder_task", columnList = "task_activity_id"),
                @Index(name = "idx_reminder_trip", columnList = "trip_activity_id"),
                @Index(name = "idx_reminder_time", columnList = "remind_at"),
                @Index(name = "idx_reminder_sent", columnList = "is_sent")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_activity_id")
    Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_activity_id")
    Trip trip;

    @Column(name = "remind_at")
    LocalDateTime remindAt;

    @Enumerated(EnumType.STRING)
    ReminderType type;

    @Column(name = "is_sent")
    Boolean isSent = false;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}