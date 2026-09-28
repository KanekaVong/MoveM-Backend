package com.movem.backend.task.entities;

import com.movem.backend.shared.activity.entities.Activity;
import com.movem.backend.shared.attachment.entities.Attachment;
import com.movem.backend.commons.enums.Task.Priority;
import com.movem.backend.commons.enums.Task.RecurringType;
import com.movem.backend.shared.checklist.entities.Checklist;
import com.movem.backend.shared.reminder.entities.Reminder;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Task", indexes = {@Index(name = "idx_task_priority", columnList = "priority"), @Index(name = "idx_task_recurring", columnList = "is_recurring") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
    @Id
    String activityId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "activity_id")
    Activity activity;

    @Enumerated(EnumType.STRING)
    Priority priority;

    @Column(name = "is_recurring")
    Boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurring_type")
    RecurringType recurringType;

    @Column(name = "recurring_interval")
    Integer recurringInterval = 1;

    @Column(name = "recurring_end_date")
    LocalDate recurringEndDate;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Checklist> checklists = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL , orphanRemoval = true)
    List<Reminder> reminders = new ArrayList<>();

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Attachment> attachments = new ArrayList<>();
}