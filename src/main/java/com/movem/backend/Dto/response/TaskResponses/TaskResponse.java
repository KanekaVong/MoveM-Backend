package com.movem.backend.Dto.response.TaskResponses;

import com.movem.backend.model.enums.Activity.ActivityStatus;
import com.movem.backend.model.enums.Priority;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponse {

    private String activityId;

    private String activityName;

    private List<TaskLabelResponse> labels;

    private List<TaskChecklistResponse> checklists;

    private List<TaskReminderResponse> reminders;

    private Integer totalChecklistItems;

    private Integer completedChecklistItems;

    private Integer checklistProgress;

    private String description;

    private ActivityStatus status;

    private Priority priority;

    private Boolean recurring;

    private LocalDateTime startActivity;

    private LocalDateTime deadline;


}