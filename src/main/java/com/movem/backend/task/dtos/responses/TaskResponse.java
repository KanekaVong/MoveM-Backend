package com.movem.backend.task.dtos.responses;

import com.movem.backend.shared.attachment.dtos.responses.AttachmentResponse;
import com.movem.backend.shared.checklist.dtos.responses.ChecklistResponse;
import com.movem.backend.shared.group.dtos.responses.GroupMemberResponse;
import com.movem.backend.commons.enums.shared.ActivityStatus;
import com.movem.backend.commons.enums.Task.Priority;
import com.movem.backend.commons.enums.Task.RecurringType;
import com.movem.backend.shared.reminder.dtos.responses.ReminderResponse;
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
    private List<ChecklistResponse> checklists;
    private List<ReminderResponse> reminders;
    private Integer totalChecklistItems;
    private Integer completedChecklistItems;
    private Integer checklistProgress;
    private String description;
    private ActivityStatus status;
    private Priority priority;
    private Boolean recurring;
    private RecurringType recurringType;
    private LocalDateTime startActivity;
    private LocalDateTime deadline;
    private List<GroupMemberResponse> members;
    private List<AttachmentResponse> attachments;

}