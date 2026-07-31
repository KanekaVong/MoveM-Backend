package com.movem.backend.entity.Tasks;

import com.movem.backend.entity.Activity.Activity;
import com.movem.backend.model.enums.Priority;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "task",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TaskChecklist> checklists = new ArrayList<>();

    @OneToMany(mappedBy = "task",
            cascade = CascadeType.ALL ,
            orphanRemoval = true)
    private List<TaskReminder> reminders = new ArrayList<>();
}