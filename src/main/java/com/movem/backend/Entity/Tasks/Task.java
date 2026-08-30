package com.movem.backend.Entity.Tasks;

import com.movem.backend.Entity.Activity.Activity;
import com.movem.backend.Entity.Attachment.Attachment;
import com.movem.backend.model.enums.Priority;
import com.movem.backend.model.enums.RecurringType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Task", indexes = {
        @Index(name = "idx_task_priority", columnList = "priority"),

        @Index(name = "idx_task_recurring", columnList = "is_recurring") })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    private String activityId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurring_type")
    private RecurringType recurringType;

    @Column(name = "recurring_interval")
    private Integer recurringInterval = 1;

    @Column(name = "recurring_end_date")
    private LocalDate recurringEndDate;

    @OneToMany(mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TaskChecklist> checklists = new ArrayList<>();

    @OneToMany(mappedBy = "task",
            cascade = CascadeType.ALL ,
            orphanRemoval = true)
    private List<TaskReminder> reminders = new ArrayList<>();

    @OneToMany(
            mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Attachment> attachments = new ArrayList<>();
}